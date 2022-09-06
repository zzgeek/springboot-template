package zzgeek.controller.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zzgeek.annotions.VisitorToken;
import zzgeek.commons.meta.NetViewFactory;
import zzgeek.commons.meta.RequestView;
import zzgeek.commons.meta.ResponseView;
import zzgeek.service.AdminService;
import zzgeek.vo.AdminsLoginVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZZGeek
 * @date 2021年10月21日 15:33
 * @description AdminsController
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminsService;

    /**
     *
     * @description 用户登录接口
     * @author ZZGeek
     * @date 2021/10/23 15:20
     * @params
    	 * @param httpServletRequest httpServletRequest
     * @return commons.meta.ResponseView
     */
    @VisitorToken
    @RequestMapping(value = "/loginAdmin", method = RequestMethod.POST)
    ResponseView loginAdmins (HttpServletRequest httpServletRequest) throws Exception{
        RequestView in = NetViewFactory.successRequest(httpServletRequest);
        return doLoginAdmins(in.getCurrentRequestParams(),in.getCurrentAdminIp());
    }

    @VisitorToken
    @RequestMapping(value = "/loginOutAdmin", method = RequestMethod.POST)
    ResponseView loginOutAdmins (HttpServletRequest httpServletRequest) throws Exception{
        RequestView in = NetViewFactory.successRequest(httpServletRequest);
        ResponseView responseView = new ResponseView();
        return responseView;
    }

    private ResponseView doLoginAdmins (JSONObject paremData,String adminIp) throws Exception{
        AdminsLoginVo adminsLoginVo = adminsService.loginAdminByAccount(paremData.getString("adminAccount"),paremData.getString("adminPassword"),adminIp);
        if(adminsLoginVo == null){
            return NetViewFactory.errorCustomizeResponse("201","本机ip:" + adminIp + " :ip不在白名单");
        }else {
            return NetViewFactory.successResponse(adminsLoginVo);
        }
    }
}
