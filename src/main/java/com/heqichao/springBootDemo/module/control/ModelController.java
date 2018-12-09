package com.heqichao.springBootDemo.module.control;

import com.alibaba.fastjson.JSONArray;
import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.util.ExcelWriter;
import com.heqichao.springBootDemo.base.util.FileUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.service.ModelAttrService;
import com.heqichao.springBootDemo.module.service.ModelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Created by heqichao on 2018-11-19.
 */
@RestController
@RequestMapping(value = "/service")
public class ModelController extends BaseController{

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelAttrService modelAttrService;

    private  Logger logger = LoggerFactory.getLogger(ModelController.class);

    @RequestMapping(value = "/queryModelType")
    ResponeResult queryTyps() {
        return new ResponeResult(modelService.queryType());
    }

    @RequestMapping(value = "/deleteModelAttr")
    ResponeResult deleteModelAttr() {
        ResponeResult responeResult =new ResponeResult();
        Integer id = getIntegerParam("id");
        if(id!=null){
            modelAttrService.deleteByAttrId(id);
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("删除数据异常，请稍后重试");
        }
        return responeResult;
    }

    @RequestMapping(value = "/saveOrUpdateModel")
    ResponeResult saveOrUpdateModel(){
        Map map = getParamMap();
        Integer modelId =getIntegerParam("id");
        String modelName = (String) map.get("modelName");
        List<Map> list =new ArrayList<>();
        String listJson =(String) map.get("list");
        try{
            list = JSONArray.parseArray(listJson,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponeResult(modelService.saveOrUpdateModel(modelId,modelName,list));
    }

    @RequestMapping(value = "/queryAllByModelId")
    ResponeResult queryAllByModelId(){
        ResponeResult responeResult =new ResponeResult();
        Integer modelId = getIntegerParam("modelId");
        if(modelId!=null){
            responeResult.setResultObj(modelService.queryModelAndAttrsByModelId(modelId));
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("获取数据异常，请稍后重试");
        }
        return responeResult;
    }

    /**
     * 分页查询模板列表
     * @return
     */
    @RequestMapping(value = "/queryModelList")
    ResponeResult queryModelList(){
        Map map = getParamMap();
        return new ResponeResult(modelService.queryUserPageModel((String) map.get("model_name")));
    }

    @RequestMapping(value = "/deleteModel")
    ResponeResult deleteModel() {
        ResponeResult responeResult =new ResponeResult();
        Map map = getParamMap();
        Integer id = getIntegerParam("id");
        if(id!=null){
            modelService.deleteByModelId(id);
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("删除模板数据异常，请稍后重试");
        }
        return responeResult;
    }

    /**
     * 查找当前用户所拥有的模板(用户设备选择模板)
     * @return
     */
    @RequestMapping(value = "/queryUserModel")
    ResponeResult queryUserModel(){
        return new ResponeResult(modelService.queryUserModel());
    }



    @RequestMapping(value = "/exportAllUserModel")
    void exportAllUserModel(){

       Map<String,List> map = modelService.queryExportInfo();
        if(map!=null){
            String[] title = new String[]{"名称","数据类型","数值类型","小数位数","单位","公式","备注"};
            String[] code = new String[]{"attr_name","data_type","value_type","number_format", "unit","expression","memo"};
            FileOutputStream fos = null;
            File file =null;
            try{
                Iterator entries = map.entrySet().iterator();
                file = FileUtil.createTempDownloadFile("模板.xls");
                fos= new FileOutputStream(file);
                // 声明一个工作薄
                HSSFWorkbook workbook = ExcelWriter.createWorkBook();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String key = (String) entry.getKey();
                    List<Map> attrList = (List<Map>) entry.getValue();
                    String modelName = key.split(ModelService.EXCEL_NAME_SPLIT)[1];
                    ExcelWriter.export(workbook,modelName,title,attrList,code);
                }
                workbook.write(fos);
            }catch (Exception e){

            }finally {
                if(fos!=null){
                    try {
                        fos.close();
                        download(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }


    }


    @RequestMapping(value = "/importModel")
    ResponeResult importModel(@RequestParam("file") MultipartFile multipartFile){
        File file =FileUtil.createTempDownloadFile( System.currentTimeMillis()+".xml");
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponeResult();
    }
}
