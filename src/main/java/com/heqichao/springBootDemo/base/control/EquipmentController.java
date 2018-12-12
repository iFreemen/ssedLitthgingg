package com.heqichao.springBootDemo.base.control;

import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.ExcelReader;
import com.heqichao.springBootDemo.base.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
    @RequestMapping(value = "/exprLora" )
    public void exprLora() throws Exception {
    	 eService.exportEquipments("Lora", "L", eService.titleLora, eService.codeLora);
    }
    @RequestMapping(value = "/exprNbiot" )
    public void exprNbiot() throws Exception {
    	eService.exportEquipments("Nbiot", "N", eService.titleNbiot, eService.codeNbiot);
    }
    @RequestMapping(value = "/exprGprs" )
    public void exprGprs() throws Exception {
    	eService.exportEquipments("GPRS", "G", eService.titleGPRS, eService.codeGPRS);
    }
    
    @RequestMapping(value = "/importLora")
    ResponeResult importModel(@RequestParam("file") MultipartFile multipartFile){
        File file =FileUtil.createTempDownloadFile( System.currentTimeMillis()+".xml");
        try {
            multipartFile.transferTo(file);

            Map map =ExcelReader.readFile(file);
            eService.saveUploadImport(map, eService.codeLora, "L");
//            modelService.saveImport(map);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            FileUtil.deleteFile(file);
        }

        return new ResponeResult("sssss");
    }

}
