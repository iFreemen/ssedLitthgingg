package com.heqichao.springBootDemo.base.control;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Muzzy Xu.
 * 
 */


@RestController
@RequestMapping(value = "/service")
public class EquipmentController extends BaseController{

    @Autowired
    private EquipmentService eService;

    
    @RequestMapping(value = "/getEquipments")
    public ResponeResult getUsers() {
    	return new ResponeResult(eService.queryEquipmentList());
    }
    @RequestMapping(value = "/getEquById")
    public ResponeResult getEquById() {
    	return new ResponeResult(eService.getEquById());
    }
    @RequestMapping(value = "/getEquPage")
    public ResponeResult queryEquipmentPage() {
    	return new ResponeResult(eService.queryEquipmentPage());
    }
    
    @RequestMapping(value = "/getEquAll")
    public List<String> getEquipmentIdListAll() {
    	return eService.getEquipmentIdListAll();
    }
    @RequestMapping(value = "/getEquEditInfo")
    public ResponeResult getEquEdit() {
    	return eService.getEquEditById();
    }
    
    @RequestMapping(value = "/addEqu" )
    @ResponseBody
    public ResponeResult addEqu(@RequestBody Map map) throws Exception {
        return eService.insertEqu(map);
    }
    @RequestMapping(value = "/editEqu" )
    @ResponseBody
    public ResponeResult editEqu(@RequestBody Map map) throws Exception {
    	return eService.editEqu(map);
    }
    
    @RequestMapping(value = "/delEqu" )
    @ResponseBody
    public ResponeResult delEqu(@RequestBody Map map) throws Exception {
    	return eService.deleteEquByID(map);
    }
    

}
