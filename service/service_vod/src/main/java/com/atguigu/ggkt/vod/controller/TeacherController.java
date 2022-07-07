package com.atguigu.ggkt.vod.controller;



import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 讲师 前端控制器
 * @author atguigu
 */
@Api(tags = "讲师管理接口")
@RestController
@RequestMapping(value="/admin/vod/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    /***
     * @describe
     *  查询所有讲师列表
     * @return java.util.List<com.atguigu.ggkt.model.vod.Teacher>
     * @author ChiHsien<br>
     * @version
     */
    @GetMapping("findAll")
    @ApiOperation("查询所有讲师列表")
    public Result findAll(){
//        try{
//            int i = 10/0;
//        }catch(Exception e){
//           throw  new GgktException(201, "执行自定义异常GgktException");
//        }

        List<Teacher> list = teacherService.list();
        return Result.ok(list);
    }

    //逻辑删除讲师
    @DeleteMapping("remove/{id}")
    @ApiOperation("逻辑删除讲师")
    public Result removeById(@ApiParam(name = "id", value = "ID", required = true)
                              @PathVariable Long id) {
        boolean isSuccess = teacherService.removeById(id);
        if (isSuccess) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }
    /***
     * @describe
     *  条件查询带分页查询讲师
     * @param: page
     * @param: limit
     * @param: teacherQueryVo
     * @return com.atguigu.ggkt.result.Result
     * @author ChiHsien<br>
     * @version
     */
    //3 条件查询分页
    @ApiOperation("条件查询分页")
    @PostMapping("findQueryPage/{current}/{limit}")
    public Result findPage(@PathVariable long current,
                           @PathVariable long limit,
                           //传json数据，条件值可以为空，查全部，要使用post提交
                           @RequestBody(required = false) TeacherQueryVo teacherQueryVo) {
        //创建page对象
        Page<Teacher> pageParam = new Page<>(current,limit);
        //判断teacherQueryVo对象是否为空
        if(teacherQueryVo == null) {//查询全部
            IPage<Teacher> pageModel =
                    teacherService.page(pageParam,null);
            return Result.ok(pageModel);
        } else {
            //获取条件值，
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();
            //进行非空判断，条件封装
            QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
            if(!StringUtils.isEmpty(name)) {
                wrapper.like("name",name);
            }
            if(!StringUtils.isEmpty(level)) {
                wrapper.eq("level",level);
            }
            if(!StringUtils.isEmpty(joinDateBegin)) {
                wrapper.ge("join_date",joinDateBegin);
            }
            if(!StringUtils.isEmpty(joinDateEnd)) {
                wrapper.le("join_date",joinDateEnd);
            }
            //调用方法分页查询
            IPage<Teacher> pageModel = teacherService.page(pageParam, wrapper);
            //返回
            return Result.ok(pageModel);
        }
    }
    /**
     * @describe
     * 添加讲师
     * @return
     * @author ChiHsien<br>
     * @version
     */
    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result saveTeacher(@RequestBody Teacher teacher) {
        boolean isSuccess = teacherService.save(teacher);
        if(isSuccess) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }
    /**
     * @describe
     * id查询讲师
     * @param: id
     * @return com.atguigu.ggkt.result.Result
     * @author ChiHsien<br>
     * @version
     */
    @ApiOperation("根据id查询")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable Long id) {
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);
    }
    /***
     * @describe
     * 修改讲师
     * @param: teacher
     * @return com.atguigu.ggkt.result.Result
     * @author ChiHsien<br>
     * @version
     */
    @ApiOperation("修改最终实现")
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody Teacher teacher) {
        boolean isSuccess = teacherService.updateById(teacher);
        if(isSuccess) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }

    /***
     * @describe
     * 批量删除讲师
     * @param: idList
     * @return com.atguigu.ggkt.result.Result
     * @author ChiHsien<br>
     * @version
     */
    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean isSuccess = teacherService.removeByIds(idList);
        if(isSuccess) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }

}