package com.supply.service.impl;

import com.supply.constant.FormattingConstant;
import com.supply.context.BaseContext;
import com.supply.entity.IdentityAuthentication;
import com.supply.entity.Report;
import com.supply.entity.User;
import com.supply.mapper.AdminMapper;
import com.supply.mapper.UserLoginMapper;
import com.supply.service.AdminService;
import com.supply.utils.EmailUtil;
import com.supply.vo.ReportInformationVO;
import com.supply.vo.UserInformationVO;
import com.supply.vo.VerificationInformationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    private final UserLoginMapper userLoginMapper;

    private final EmailUtil emailUtil;

    /**
     * 个人信息回显
     * @return 用户信息
     */
    public UserInformationVO getInformation() {
        User user = userLoginMapper.getUserInformationById(BaseContext.getCurrentId());
        UserInformationVO userInformationVO = new UserInformationVO();
        BeanUtils.copyProperties(user,userInformationVO);
        log.info("当前登录的管理员信息：{}",userInformationVO);
        return userInformationVO;
    }

    /**
     * 申请认证信息查询
     *
     * @param type 工种编号，1为医护端，2为供应端
     * @return 待审核的身份信息
     */
    public List<VerificationInformationVO> getVerificationInformation(Long type) {
        List<IdentityAuthentication> verificationInformation = adminMapper.getVerificationInformation(type);
        log.info("工种编号为{}的全体申请信息为：{}", type, verificationInformation);
        if (verificationInformation != null && !verificationInformation.isEmpty()) {
            List<VerificationInformationVO> list = new ArrayList<>();
            for (IdentityAuthentication identityAuthentication : verificationInformation) {
                log.info("当前申请信息为：{}", identityAuthentication);
                VerificationInformationVO verificationInformationVO = new VerificationInformationVO();
                BeanUtils.copyProperties(identityAuthentication, verificationInformationVO);
                verificationInformationVO.setApplicationTime(identityAuthentication.getApplicationTime().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
                User user = userLoginMapper.getUserInformationById(identityAuthentication.getUserId());
                verificationInformationVO.setUsername(user.getUsername());
                verificationInformationVO.setFirmName(user.getFirmName());
                verificationInformationVO.setImages(Arrays.stream(identityAuthentication.getImages().split(",")).toList());
                log.info("查询到的数据信息为：{}", verificationInformationVO);
                list.add(verificationInformationVO);
            }
            log.info("此次所有的申请信息为：{}", list);
            return list;
        } else {
            log.info("工种编号{}下暂时没有新的申请信息", type);
            return null;
        }
    }

    /**
     * 身份信息审核接口
     *
     * @param id      身份信息申请id
     * @param isAgree 是否同意，1为是，2为否
     */
    public void checkVerificationInformation(Long id, Long isAgree) {
        log.info("管理员对于申请编号为{}的认证做出决定：{}", id, isAgree);
        //先获取这个申请是哪个用户发起的
        Long userId = adminMapper.getApplyUser(id);
        //再获取这个用户的邮箱信息
        String email = userLoginMapper.getUserInformationById(userId).getEmail();
        if (isAgree == 1) {
            //将用户表中的账户状态改为可使用
            userLoginMapper.changeStatusToNormal(userId);
            //对用户的申请信息做修改
            adminMapper.checkSuccessfully(id, BaseContext.getCurrentId(), LocalDateTime.now());
            //给用户发邮件进行提醒
            emailUtil.Mail(email, "你在药品供应链系统的账户信息审核已通过，即刻可使用全部功能，具体信息请登录账户查看" + "\n" + "审核人编号：" + BaseContext.getCurrentId());
        } else {
            //将用户表中的账户状态改为审核失败
            userLoginMapper.changeStatusToCheckFailed(userId);
            //对用户的申请信息做修改
            adminMapper.checkUnsuccessfully(id, BaseContext.getCurrentId());
            //给用户发邮件进行提醒
            emailUtil.Mail(email, "你在药品供应链系统的账户信息审核未通过，请检查后重新上传" + "\n" + "审核人编号：" + BaseContext.getCurrentId());
        }
    }

    /**
     * 举报信息查询接口
     *
     * @return 举报信息
     */
    public List<ReportInformationVO> getReportInformation() {
        log.info("开始查询所有举报信息");
        List<Report> reports = adminMapper.getAllReportInformation();
        List<ReportInformationVO> list = new ArrayList<>();
        for (Report report : reports) {
            log.info("当前举报信息为：{}", report);
            ReportInformationVO reportInformationVO = new ReportInformationVO();
            BeanUtils.copyProperties(report, reportInformationVO);
            reportInformationVO.setReportTime(report.getReportTime().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
            reportInformationVO.setImages(Arrays.stream(report.getImages().split(",")).toList());
            reportInformationVO.setFirmName(userLoginMapper.getUserInformationById(report.getUserId()).getFirmName());
            reportInformationVO.setInformerFirmName(userLoginMapper.getUserInformationById(report.getReportUserId()).getFirmName());
            list.add(reportInformationVO);
        }
        return list;
    }

    /**
     * 处理举报信息
     *
     * @param id        举报id
     * @param isIllegal 是否违规，1为是，2为否
     */
    public void dealReport(Long id, Integer isIllegal) {
        log.info("管理员对于举报id为：{}做出处理：{}",id,isIllegal);
        List<Report> list = adminMapper.getAllReportInformation().stream().filter(s -> Objects.equals(s.getId(), id)).toList();
        //先获取举报人和被举报人id
        Long reportUserId = list.get(0).getReportUserId();
        log.info("举报人id：{}",reportUserId);
        Long userId = list.get(0).getUserId();
        log.info("被举报人id：{}",userId);
        //获取举报人邮箱
        String reportUserEmail = userLoginMapper.getUserInformationById(reportUserId).getEmail();
        log.info("举报人邮箱：{}",reportUserEmail);
        //再获取被举报人邮箱
        String userEmail = userLoginMapper.getUserInformationById(userId).getEmail();
        log.info("被举报人邮箱：{}",userEmail);
        if (isIllegal == 1) {
            //将信息改为已处理
            adminMapper.dealReport(id);
            //向举报人发邮件告知举报不成功
            emailUtil.Mail(reportUserEmail, """
                    你好，
                    经过我们的核实，发现你的举报对象暂时不存在违规行为，因而举报不成立。
                    我们将持续关注，若发现其有违反规定的行为，将采取封号措施，感谢你对供应系统做出的贡献。""");
        } else {
            //将信息改为已处理
            adminMapper.dealReport(id);
            //将被举报人的账户封禁
            userLoginMapper.blockAccount(userId);
            //向举报人发送邮件告知举报成功
            emailUtil.Mail(reportUserEmail, """
                    你好，
                    经过我们的核实，发现你的举报对象确实存在违规行为，因而举报成立。
                    我们已对其采取封号措施，感谢你对供应系统做出的贡献。""");
            //再向被举报人发送邮件
            emailUtil.Mail(userEmail, """
                    你好，
                    你已被举报。经过我们的核实，发现你的账号确实存在违规行为。
                    我们已对你的账号进行封号处理，如有异议请联系管理人员。""");
        }
    }
}
