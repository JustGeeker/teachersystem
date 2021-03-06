package com.just.teachersystem.Controller;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import com.github.andyczy.java.excel.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.teachersystem.Annotation.Logs;
import com.just.teachersystem.Entity.Kind;

import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.RedisUtils;
import com.just.teachersystem.VO.BonusInfo;
import com.just.teachersystem.VO.PerformanceInfo;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/online/root")
public class RootController {
    @Autowired
    RootService root;
    @Autowired
    RedisUtils redisUtils;


    /**
     * 控制信息录入入口开关
     * @param map
     * @return
     */
    @Logs(role="root",description = "控制信息录入入口开关")
    @PostMapping("/manageUserEntrance")
    public JsonData manageUserEntrance(@RequestBody Map<String, Object> map){

        boolean res=redisUtils.hmset("Entrance:user",map);
        if(res) return JsonData.buildSuccess("操作成功");
        return JsonData.buildError("操作失败");

    }

    /**
     * 控制管理员获取入口开关
     * @param map
     * @return
     */
    @Logs(role="root",description = "控制管理员获取入口开关")
    @PostMapping("/manageAdminEntrance")
    public JsonData manageAdminEntrance(@RequestBody Map <String,Object> map) {

        boolean res=redisUtils.hmset("Entrance:admin",map);
        if(res) return JsonData.buildSuccess("操作成功");
        return JsonData.buildError("操作失败");
    }


    /**
     * 添加分类
     * @param kind
     * @return
     */
    @Logs(role="root",description = "root添加分类")
    @PostMapping("/addType")
    public JsonData addType(@RequestBody Kind kind){
        boolean is = root.addType(kind);
        if(is){
            return JsonData.buildSuccess("添加成功");
        }
        return JsonData.buildError("添加失败");

    }

    /**
     * 删除类别
     * @param kind
     * @return
     */
    @Logs(role="root",description = "root删除类别")
    @PostMapping("/deleteType")
    public JsonData deleteType(@RequestBody Kind kind){
//        System.out.println(kind);
        boolean is = root.deleteType(kind);
        if(is){
            return JsonData.buildSuccess("删除成功");
        }
        return JsonData.buildError("删除失败");
    }



    /**
     * 添加级别
     * @param level
     * @return
     */
    @Logs(role="root",description = "root添加级别")
    @PostMapping("/addLevel")
    public JsonData addType(@RequestParam("level") String level) {
        boolean is = root.addLevel(level);
        if(is){
            return JsonData.buildSuccess("添加成功");
        }
        return JsonData.buildError("添加失败");
    }

    /**
     * 删除级别
     * @param level
     * @return
     */
    @PostMapping("/deleteLevel")
    @Logs(role="root",description = "root删除级别")
    public JsonData deleteLevel(@RequestParam("level") String level){
        boolean is=root.deleteLevel(level);
        if(is){
            return JsonData.buildSuccess("删除成功");
        }
        return JsonData.buildError("删除失败");
    }

    /**
     * 添加奖项
     * @param prize
     * @return
     */
    @Logs(role="root",description = "root添加奖项")
    @PostMapping("/addPrize")
    public JsonData addPrize(@RequestParam("prize") String prize) {
        boolean is = root.addPrize(prize);
        if(is){
            return JsonData.buildSuccess("添加成功");
        }
        return JsonData.buildError("添加失败");
    }

    /**
     * 删除奖项
     * @param prize
     * @return
     */
    @PostMapping("/deletePrize")
    @Logs(role="root",description = "root删除奖项")
    public JsonData deletePrize(@RequestParam("prize") String prize){
        boolean is=root.deletePrize(prize);
        if(is){
            return JsonData.buildSuccess("删除成功");
        }
        return JsonData.buildError("删除失败");
    }

    /**
     * 添加用户信息
     * @param userInfo
     * @return
     */
    @Logs(role="root",description = "root添加用户信息")
    @PostMapping("/addUserInfo")
    public JsonData addUserInfo(@RequestBody UserInfo userInfo) {
        if (userInfo==null||userInfo.getWorknum()==null||userInfo.getDptname()!=null) return JsonData.buildError("信息填写不完整");
        boolean res=root.addUser(userInfo);
        if (res)
            return JsonData.buildSuccess("添加成功");
        return JsonData.buildError("添加失败");
    }




    /**
     * 更新用户信息(包括修改密码和权限)
     * @param userInfo
     * @return
     */
    @Logs(role="root",description = "root更新用户信息(包括修改密码和权限)")
    @PostMapping("/updateUserInfo")
    public JsonData updateUserInfo( @RequestBody UserInfo userInfo){
//        System.out.println(userInfo);
        if (userInfo==null ||userInfo.getWorknum()==null ) return JsonData.buildError("信息填写不完整");
        boolean res=root.updateUserInfo(userInfo);
        if(res){
            return JsonData.buildSuccess("设置成功");
        }
        return JsonData.buildError("设置失败");
    }

    /**
     * 设置用户权限
     * @return
     */
    @Logs(role="root",description = "root修改密码")
    @PostMapping("/updateUserPassword")
    public JsonData updateUserPassword(@RequestBody Map <String,String> map){
        String worknum=map.get("worknum");
        String password = map.get("password");
        if(worknum==null || password == null){
            return JsonData.buildError("参数有误");
        }
        UserInfo userInfo=new UserInfo();
        userInfo.setWorknum(worknum);
        userInfo.setPassword(password);
        System.out.println(userInfo);
        boolean res=root.updateUserInfo(userInfo);
        return JsonData.buildSuccess(res);
    }

    /**
     * 根据工号删除用户
     * 工号worknum
     * @return
     */
    @Logs(role="root",description = "root根据工号删除用户")
    @PostMapping("/deleteUser")
    public JsonData deleteUser(@RequestBody Map<String, String> map){
        String worknum=map.get("worknum");

        if(worknum==null || worknum.equals("")){
            return JsonData.buildError("工号为空");
        }
        boolean res = root.deleteUser(worknum);
        if(res)
            return JsonData.buildSuccess("删除成功");
        return JsonData.buildError("删除失败");
    }

    /**
     * 根据条件筛选信息
     * @param page
     * @param size
     * @return
     */
    @Logs(role="root",description = "root根据条件筛选用户信息")
    @PostMapping("/getUserList")
    public JsonData getUserList(@RequestBody UserInfo userInfo,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "30")int size){

        PageHelper.startPage(page,size);
        List list=root.getUserInfo(userInfo);
        if(userInfo==null) return JsonData.buildError("服务器出错");
        PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo> (list);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 下载用户表
     */
    @Logs(role="root",description = "root下载用户表")
    @PostMapping("/getUserExcel")
    public void getUserExcel(HttpServletResponse response) {
        UserInfo userInfo = new UserInfo();
        List<UserInfo>list=root.getUserInfo(userInfo);
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工教职工人信息表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"部门","姓名","工号","性别","出生年月",
                "入校时间","电话","专业技术职称","最高学历","最高学位","授学位单位名称","获最高学位的专业名称","是否双师型",
                "是否具有行业背景","是否博硕士生导师"};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);

        String []values=null;

        for (UserInfo p:list) {

            values= new String[]{p.getDptname(), p.getName(),p.getWorknum(),p.getGender(), String.valueOf(p.getBirthday()),
                    String.valueOf(p.getEnterTime()),p.getPhone(),p.getTechTittle(),p.getEduBgd(),p.getDegree(),p.getSchool(),
                    p.getMajor(),p.getDoubleTeacher()==0?"否":"是", p.getBackground()==0?"否":"是", p.getTutor()==0?"否":"是"};

            a.add(values);
        }

        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse(response);
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 条件筛选选业绩分信息
     * year 年度
     * department 部门
     * master  负责人
     * @return
     */
    @Logs(role="root",description = "root条件筛选选业绩分信息")
    @PostMapping("/getPerformanceInfo")
    public JsonData getPerformanceInfo(
            @RequestBody PerformanceInfo performanceInfo,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size" ,defaultValue ="10")int size ){
        PageHelper.startPage(page,size);

        if(performanceInfo==null){
            return JsonData.buildError("参数错误");
        }
        performanceInfo.setStatus(1);
        List list=root.getPerfromanceList(performanceInfo);


        if(list==null) return JsonData.buildError("服务器错误");
        PageInfo pageInfo=new PageInfo(list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 条件导出业绩信息
     * @param response
     *
     *  year 年度
     *
     */
    @Logs(role="root",description = "root条件导出业绩信息")
    @PostMapping("/getPerformanceExcel")
    public void getPerformanceExcel(HttpServletResponse response,@RequestParam("year")String year){

        PerformanceInfo performanceInfo=new PerformanceInfo();
        performanceInfo.setYear(year);
        performanceInfo.setStatus(1);
        List<PerformanceInfo> list=root.getPerfromanceList(performanceInfo);

        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={year+"校区、苏理工教师业绩信息表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"院部","业绩分计算科室","分类—类别","立项年度","项目名称", "负责人","业绩分(分)"};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);

        String []values=null;

        for (PerformanceInfo p:list) {
            values= new String[]{p.getDepartment(), p.getComputeoffice(),p.getType(),p.getYear(), p.getProject(), p.getMaster(), String.valueOf(p.getPoints())};
            a.add(values);
        }
        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }


    /**
     * 根据id 删除业绩分信息(删除时status 置0)
     *  id
     * @return
     */
    @Logs(role="root",description = "root根据id 删除业绩分信息(删除时status 置0)")
    @PostMapping("/deletePerformance")
    public JsonData deletePerformance(@RequestBody  PerformanceInfo p){
        if(p.getId()<0) return JsonData.buildError("参数出错");
        p.setStatus(0);
        if(root.updatePerformanceInfo(p))
            return JsonData.buildSuccess("删除成功");
        return  JsonData.buildError("删除失败");
    }

    /**
     * 修改业绩信息
     * @param performanceInfo
     * @return
     */
    @Logs(role="root",description = "root修改业绩信息")
    @PostMapping("/updatePerformance")
    public JsonData updatePerformance(@RequestBody PerformanceInfo performanceInfo){
        if(performanceInfo==null) return JsonData.buildError("传过来的值为空");
        performanceInfo.setStatus(1);
        if(root.updatePerformanceInfo(performanceInfo))
            return JsonData.buildSuccess("修改成功");
        return  JsonData.buildError("修改失败");
    }

    /**
     * 添加业绩信息
     * @param performance
     * @return
     */
    @Logs(role="root",description = "root添加业绩信息")
    @PostMapping("/addPerformance")
    public JsonData addPerformance(@RequestBody PerformanceInfo performance) {
        if(performance==null) return JsonData.buildError("传过来的值为空");
        if(root.addPerformanceInfo(performance))
            return JsonData.buildSuccess("添加成功");
        return  JsonData.buildError("添加失败");
    }


    /**
     *条件筛选奖金信息
     * department 部门
     * year 年度
     * master 负责人
     * @param page
     * @param size
     * @return
     */
    @Logs(role="root",description = "root条件筛选奖金信息")
    @PostMapping("/getBonusInfo")
    public JsonData getBonusInfo( @RequestBody BonusInfo bonusInfo,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10")int size){
            PageHelper.startPage(page,size);

        if(bonusInfo==null) return JsonData.buildError("参数出错");
        List list=root.getBonusList(bonusInfo);


        if(list==null) return JsonData.buildError("服务器错误");
        PageInfo pageInfo=new PageInfo(list);
        return JsonData.buildSuccess(pageInfo);

    }


    /**
     * 条件导出奖金信息
     * @param response
     * @param year
     */
    @Logs(role="root",description = "root条件导出奖金信息")
    @PostMapping("/getBonusExcel")
    public void getBonusExcel(HttpServletResponse response, @RequestParam(value = "year",defaultValue ="") String year ){
        BonusInfo bonusInfo=new BonusInfo();
        bonusInfo.setYear(year);
        List<BonusInfo> list=root.getBonusList(bonusInfo);
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={year+"校区、苏理工教师奖金信息表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"院部","奖金计算科室(内置)","分类—类别","立项年度","项目名称", "负责人","奖金(元)"};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);

        String []values=null;

        for (BonusInfo b:list) {
            values= new String[]{b.getDepartment(), b.getComputeoffice(),b.getType(),b.getYear(), b.getProject(), b.getMaster(), String.valueOf(b.getBonus())};
            a.add(values);
        }
        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 根据id 删除奖金信息
     * @return 将status置为0
     */
    @Logs(role="root",description = "root根据id 删除奖金信息")
    @PostMapping("/deleteBonus")
    public JsonData deleteBonus(@RequestBody  BonusInfo b){
        if(b.getId()<0) return JsonData.buildError("参数出错");
        b.setStatus(0);
        if(root.updateBonusInfo(b))
            return JsonData.buildSuccess("删除成功");
        return  JsonData.buildError("删除失败");
    }

    /**
     * 修改奖金信息
     * @param bonusInfo
     * @return
     */
    @Logs(role="root",description = "root修改奖金信息")
    @PostMapping("/updateBonus")
    public JsonData updateBonus(@RequestBody BonusInfo bonusInfo){
        if(bonusInfo==null) return JsonData.buildError("传过来的值为空");
        System.out.println(bonusInfo.getId());
        if(root.updateBonusInfo(bonusInfo))
            return JsonData.buildSuccess("修改成功");
        return  JsonData.buildError("修改失败");
    }

    /**
     * 添加奖金信息
     * @param bonusInfo
     * @return
     */
    @Logs(role="root",description = "root添加奖金信息")
    @PostMapping("/addBonus")
    public JsonData addBonus(@RequestBody BonusInfo bonusInfo) {
        if(bonusInfo==null) return JsonData.buildError("传过来的值为空");
        if(root.addBonusInfo(bonusInfo))
            return JsonData.buildSuccess("添加成功");
        return  JsonData.buildError("添加失败");
    }


}
