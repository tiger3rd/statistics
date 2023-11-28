package com.piesat.statistics.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.piesat.statistics.bean.MonitorCheckUserInfo;
import com.piesat.statistics.bean.WhiteList;
import com.piesat.statistics.service.MonitorCheckUserService;
import com.piesat.statistics.service.WhiteListService;
import com.piesat.statistics.bean.SysSession;
import com.piesat.statistics.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/checkuser")
public class CheckUserController {
    private final static Logger logger = LoggerFactory.getLogger(CheckUserController.class);
    @Autowired
    private MonitorCheckUserService monitorCheckUserService;

    @Autowired
    private WhiteListService whiteListService;

    @Value("${emailUrl}")
    private String emailUrl;


    /**
     * 账户列表
     * @param request
     * @param department
     * @param checkStatus
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/getproblemList")
    public Map<String, Object> getproblemList(HttpServletRequest request, String department,String checkStatus,String type,String startTime, String endTime,
                                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                              @RequestParam(value = "orderName", required = false, defaultValue = "user_id") String orderName,
                                              @RequestParam(value = "orderType", required = false, defaultValue = "desc") String orderType) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }else{
            //只能查看本单位下的数据
            department = tokenDepartment;
        }

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize, orderName + " " + orderType);
            List<Map<String, Object>> maps = monitorCheckUserService.getList("".equals(department)?null:department,"".equals(checkStatus)?null:checkStatus, "".equals(type)?null:type);

            long startRow = objects.getStartRow();
            if(maps!=null&&maps.size()>0){
                for(int i = 0;i<maps.size();i++){
                    maps.get(i).put("ROWNO",startRow+1+i);
                }
            }
            ret.put("data",maps);

            Map pageMap = new TreeMap();
            pageMap.put("total",objects.getTotal());
            pageMap.put("pageNum",objects.getPageNum());
            pageMap.put("pageSize",objects.getPageSize());
            pageMap.put("pages",objects.getPages());
            pageMap.put("startRow",objects.getStartRow());
            pageMap.put("endRow",objects.getEndRow());
            pageMap.put("orderBy",objects.getOrderBy());
            ret.put("pageinfo",pageMap);
        } catch (Exception e) {
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    //审核账户
    @RequestMapping("/check")
    public Map<String, Object> checkUser(HttpServletRequest request, Integer id, String checkDayLimit, String checkdesc, String emailTitle, String email, String emailContent) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String username = sysSession.getUsername();

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            //根据ID查询待审核记录
            MonitorCheckUserInfo monitorCheckUserInfo = monitorCheckUserService.getUserById(id);
            //更新审核状态
            monitorCheckUserInfo.setCheckStatus("2");
            monitorCheckUserInfo.setCheckTime(new Date());
            monitorCheckUserInfo.setCheckDayLimit(checkDayLimit);
            monitorCheckUserInfo.setCheckdesc(checkdesc);
            //发送邮件,待定。。。
            //boolean sendFlag = EmailUtil.postMail(emailUrl, email, emailTitle, emailContent);
            boolean sendFlag = true;
            //保存发送邮件的地址及内容
            monitorCheckUserInfo.setEmailTitle(emailTitle);
            monitorCheckUserInfo.setTargetEmail(email);
            monitorCheckUserInfo.setEmailContent(emailContent);
            if(sendFlag){
                monitorCheckUserInfo.setEmailPushStatus("2");//发送成功
            }else{
                monitorCheckUserInfo.setEmailPushStatus("1");//发送失败
            }

            monitorCheckUserInfo.setUpdateTime(new Date());
            monitorCheckUserInfo.setUpdateBy(username);

            monitorCheckUserService.updateUserCheck(monitorCheckUserInfo);
        } catch (Exception e) {
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    //处理反馈
    @RequestMapping(value = "feedback", method = RequestMethod.POST)
    public Map<String, Object> dealUser(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile uploadFile,
                                        Integer id, String feedbackStatus, String measureStatus, String feedbackdesc){
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String username = sysSession.getUsername();

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            //根据ID查询待审核记录
            MonitorCheckUserInfo monitorCheckUserInfo = monitorCheckUserService.getUserById(id);

            //反馈状态结果：1：正常:、2:异常
            //处置措施：1：暂时保留-过了本周期后进行冻结；2、长期保留-不进行冻结：3、直接冻结
            //正常状态下：处置措施有暂时保留和长期保留两种
            //异常：处置措施是直接冻结

            //本系统用到上述转态的地方主要是：1、页面查询top5或者top50等等时，不查询已邮件核实的账户信息
            //2、自动冻结任务，查询反馈不正常需要冻结的账户进行冻结操作，会真正影响到账户的状态，从而限制用户接口使用
            monitorCheckUserInfo.setFeedbackStatus(feedbackStatus);
            monitorCheckUserInfo.setMeasureStatus(measureStatus);
            if("3".equals(measureStatus)){
                logger.info("冻结账户"+monitorCheckUserInfo.getUser_id());
            }else if("2".equals(measureStatus)){
                WhiteList whiteList = new WhiteList();
                whiteList.setUser_id(monitorCheckUserInfo.getUser_id());
                whiteList.setInvalid(0);
                whiteList.setCreateTime(new Date());
                whiteList.setCreateBy(username);
                whiteListService.save(whiteList);
            }

            monitorCheckUserInfo.setFeedbackdesc(feedbackdesc);
            monitorCheckUserInfo.setUpdateTime(new Date());
            monitorCheckUserInfo.setUpdateBy(username);

            if(uploadFile!=null){
                //文件存储
                String originalFilename = uploadFile.getOriginalFilename();
                String parentPath = "";
                if(!(System.getProperty("os.name").startsWith("win")||System.getProperty("os.name").startsWith("Win"))){
                    parentPath = "/opt/tianqing/feedbackfile";
                }else{
                    parentPath = "C:\\Users\\admin\\Desktop\\天擎监管平台设计开发\\";
                }
                File parentDir = new File(parentPath);
                if(!parentDir.exists()){
                    parentDir.mkdirs();
                }
                String destPath = parentPath+File.separator+originalFilename;
                File desFile = new File(destPath);
                try {
                    if(desFile.exists()){
                        desFile.delete();
                    }
                    uploadFile.transferTo(desFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                monitorCheckUserInfo.setFeedbackfilepath(destPath);
            }
            monitorCheckUserService.updateUserFeedback(monitorCheckUserInfo);

        } catch (Exception e) {
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }
}
