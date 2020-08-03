package com.hanjie.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanjie.web.dao.UserDao;
import com.hanjie.web.entity.Account;
import com.hanjie.web.entity.Organ;
import com.hanjie.web.entity.User;
import com.hanjie.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.sdk.keystore.KeystoreUtility;
import org.sdk.transaction.TransactionUtility;
import org.sdk.util.HttpclientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.tdf.common.util.HexBytes;
import org.tdf.crypto.keystore.KeyStoreImpl;
import org.tdf.crypto.keystore.SMKeystore;
import org.tdf.sunflower.types.Transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web")
@Slf4j
public class loginController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    //植入对象
    @Autowired
    UserService service;

    @Autowired
    UserDao userDao;

    @GetMapping(value = "/login")
    public String login(HttpServletRequest request){
        log.info("========登陆=========");
        return "login";
    }


    @GetMapping(value = "/header")
    public String header(HttpServletRequest request){
        log.info("=========头页面加载=========");
        return "header";
    }

    @PostMapping(value = "/user/index")
    public String index(HttpServletRequest request){
        log.info("=========主页=========");
        return "index";
    }

    @GetMapping(value = "/user/getAddress")
    @ResponseBody
    public APIResponse getAddress(String keystore,
            HttpServletRequest request){
        APIResponse apiResponse = new APIResponse();
        JSONObject jsonObject = JSONObject.parseObject(keystore);
        String address = jsonObject.getString("address");
        apiResponse.setData(address);
        return apiResponse;
    }

    /**
     * 用户登录
     * @param keystore
     * @param password
     * @param map
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/user/login")
    public String login_1(@RequestParam(value = "keystore1") String keystore,
                        @RequestParam(value = "password") String password,
                          @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
                        Map<String,Object> map, HttpSession session,Model model,HttpServletRequest request) throws Exception {
        Class.forName("org.sdk.util.Init");
        KeyStoreImpl ks = MAPPER.readValue(keystore, KeyStoreImpl.class);
//        byte[] key = SMKeystore.decryptKeyStore(ks, password);
//        JSONObject jsonObject = JSONObject.parseObject(keystore);
//        JSONObject jsonObject_cry = JSONObject.parseObject(jsonObject.getString("crypto"));
//        Crypto crypto = new Crypto(jsonObject_cry.getString("cipher"),HexBytes.fromBytes(HexBytes.decode(jsonObject_cry.getString("cipherText"))),HexBytes.fromBytes(HexBytes.decode(jsonObject_cry.getString("iv"))),HexBytes.fromBytes(HexBytes.decode(jsonObject_cry.getString("salt"))));
//        KeyStoreImpl keyStore = new KeyStoreImpl(HexBytes.fromBytes(HexBytes.decode(jsonObject.getString("publicKey"))),crypto,jsonObject.getString("id"),jsonObject.getString("version"),
//                HexBytes.fromBytes(HexBytes.decode(jsonObject.getString("mac"))),jsonObject.getString("kdf"),HexBytes.fromBytes(key));
        //获取私钥
        String privateKey = KeystoreUtility.decryptKeyStore(ks,password);
        //得到地址
        String address = KeystoreUtility.publicKeyToAddress(KeystoreUtility.privateKeyToPublicKey(privateKey));
        Account login_1 = service.loginByName_1(address);
        if(login_1 != null){
            session.setAttribute("loginUser",login_1.getUsername());
            if(login_1.getRole() == 0){
                int pageSize = 10;
                int totalRecord = service.auditCount();
                int totalPage;
                int offset;
                PageModel pm = PageModel.newPageModel(pageSize, pageNum, totalRecord);
                if(totalRecord == 0){
                    totalPage = 1;
                    offset = 0;
                }else {
                    totalPage = pm.getTotalPage();
                    pm.setCurrentPage(pageNum);
                    offset = pm.getOffset();
                }
                //待审核列表
                List<Account> list_1 = service.auditUser(pageSize, offset);
                for(int i = 0;i<list_1.size();i++){
                    String s_utf8 = new String(list_1.get(i).getAddress(),"UTF-8");
                    list_1.get(i).setAddress_utf8(s_utf8);
                    Organ organ = service.selectOrganName(String.valueOf(list_1.get(i).getOrgan()));
                    list_1.get(i).setOrganName(organ.getOrganName());
                }
                //用户列表
                List<Account> list_2 = service.auditUser_2(pageSize, offset);
                for(int i = 0;i<list_2.size();i++){
                    String s_utf8 = new String(list_2.get(i).getAddress(),"UTF-8");
                    list_2.get(i).setAddress_utf8(s_utf8);
                    Organ organ = service.selectOrganName(String.valueOf(list_2.get(i).getOrgan()));
                    list_2.get(i).setOrganName(organ.getOrganName());
                }
                session.setAttribute("address",address);
                session.setAttribute("privateKey",privateKey);
                model.addAttribute("list1",list_1);
                model.addAttribute("list2",list_2);
                model.addAttribute("totalPage",totalPage);
                if(totalRecord == 0){
                    model.addAttribute("pageNum", 1);
                }else {
                    model.addAttribute("pageNum", pm.getCurrentPage());
                }
                model.addAttribute("amount",totalRecord);
                return "affair";
            }
            return "index";
        }else{
            map.put("msg","用户名或者密码错误");
            //登陆
            return "login";
        }
    }

    @GetMapping("/user/getList")
    public String getList(@RequestParam(defaultValue = "1",value = "page") Integer pageNum,
                          @RequestParam(defaultValue = "1",value = "type") Integer type,
                          Map<String,Object> map, HttpSession session,Model model) throws UnsupportedEncodingException {
        log.info("===========第"+pageNum+"页==========");
        System.out.println(type);
        int pageSize = 10;
        int totalRecord = service.auditCount();
        int totalPage;
        int offset;
        PageModel pm = PageModel.newPageModel(pageSize, pageNum, totalRecord);
        if(totalRecord == 0){
            totalPage = 1;
            offset = 0;
        }else {
            totalPage = pm.getTotalPage();
            pm.setCurrentPage(pageNum);
            offset = pm.getOffset();
        }
        //待审核列表
        List<Account> list_1 = service.auditUser(pageSize, offset);
        for (int i = 0; i < list_1.size(); i++) {
            String s_utf8 = new String(list_1.get(i).getAddress(), "UTF-8");
            list_1.get(i).setAddress_utf8(s_utf8);
            Organ organ = service.selectOrganName(String.valueOf(list_1.get(i).getOrgan()));
            list_1.get(i).setOrganName(organ.getOrganName());
        }
        model.addAttribute("list1", list_1);
        model.addAttribute("totalPage", totalPage);
        if (totalRecord == 0) {
            model.addAttribute("pageNum", 1);
        } else {
            model.addAttribute("pageNum", pm.getCurrentPage());
        }
        model.addAttribute("amount", totalRecord);
        return "affair";
    }

    @GetMapping("/user/getUserList")
    public String getUserList(@RequestParam(defaultValue = "1",value = "page") Integer pageNum,
                          @RequestParam(defaultValue = "1",value = "type") Integer type,
                          Map<String,Object> map, HttpSession session,Model model) throws UnsupportedEncodingException {
        log.info("===========第"+pageNum+"页==========");
        System.out.println(type);
        int pageSize = 10;
        int totalRecord = service.userCount();
        int totalPage;
        int offset;
        PageModel pm = PageModel.newPageModel(pageSize, pageNum, totalRecord);
        if(totalRecord == 0){
            totalPage = 1;
            offset = 0;
        }else {
            totalPage = pm.getTotalPage();
            pm.setCurrentPage(pageNum);
            offset = pm.getOffset();
        }
        //用户列表
        List<Account> list_2 = service.auditUser_2(pageSize, offset);
        for(int i = 0;i<list_2.size();i++){
            String s_utf8 = new String(list_2.get(i).getAddress(),"UTF-8");
            list_2.get(i).setAddress_utf8(s_utf8);
            Organ organ = service.selectOrganName(String.valueOf(list_2.get(i).getOrgan()));
            list_2.get(i).setOrganName(organ.getOrganName());
        }
        model.addAttribute("list2", list_2);
        model.addAttribute("totalPage", totalPage);
        if (totalRecord == 0) {
            model.addAttribute("pageNum", 1);
        } else {
            model.addAttribute("pageNum", pm.getCurrentPage());
        }
        model.addAttribute("amount", totalRecord);
        return "affairpend";
    }

    @GetMapping("/user/layout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("loginUser");
        session.removeAttribute("password");
        session.removeAttribute("address");
        request.getSession().invalidate();
        return "login";
    }

    @GetMapping("/user/psd")
    public String psd(HttpServletRequest request){
        log.info("=========注册=========");
        return "psd";
    }

    @PostMapping("/user/register")
    public String register(@RequestParam(value = "password_1") String password_1,
                           @RequestParam(value = "password_2") String password_2,
                           Model model,Map<String,Object> map) throws Exception {
        log.info("=========注册1=========");
        if(password_1.equals(password_2) && !password_1.equals(null) && !password_1.equals("") && !password_2.equals(null) && !password_2.equals("")){
            Class.forName("org.sdk.util.Init");
            KeyStoreImpl keystore =  KeystoreUtility.generateKeyStore(password_1);
            HexBytes privateKey = keystore.getPrivateKey();
            KeyStoreImpl keyJson = SMKeystore.generateKeyStore(password_1, privateKey.getBytes());
            String json = MAPPER.writeValueAsString(keyJson);
            JSONObject jsonObject = JSONObject.parseObject(json);
            String address = KeystoreUtility.publicKeyToAddress(jsonObject.getString("publicKey"));
            model.addAttribute("keystore",json.toString());
            model.addAttribute("address",address);
            model.addAttribute("password",password_1);
            return "register";
        }else if(password_1.equals(null) || password_1.equals("") || password_2.equals(null) || password_2.equals("")){
            return "psd";
        } else {
            return "psd";
        }
    }

    @PostMapping("/user/register_2")
    @ResponseBody
    public APIResponse register_2(@RequestBody User user) throws Exception {
        log.info("=========注册2=========");
        APIResponse response = new APIResponse();
        if(user.getUsername() =="" || user.getUsername() == null || user.getCountry() =="" || user.getCountry() == null){
            response.setCode("500");
        }else{
            //判断用户名是否存在
            Account register = service.registerByName(user.getUsername());
            if(!StringUtils.isEmpty(register)){
                response.setCode("500");
                response.setMsg("用户名已存在!");
            }else{
                //注册时用户状态为待审核
                int status = 0;
                boolean register_2 = service.addAccount(user.getAddress().getBytes(),user.getUsername(),user.getRole(),user.getCountry(),status);
                if(register_2){
                    response.setCode("200");
                    response.setMsg("注册成功!");
                    //写入txt文件
                    try {
                        //String encryptData = json.toString();
                        //写入指定文件中
                        BufferedWriter bw = new BufferedWriter(new FileWriter("c:\\\\keystore.txt"));
                        bw.write(user.getKeystore());
                        bw.close();
                    } catch (Exception e) {
                        throw e;
                    }
                }else{
                    response.setCode("500");
                    response.setMsg("注册失败!");
                }
            }
        }
        return response;
    }

    @GetMapping("/user/audit")
    public String audit(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
            Model model,HttpServletRequest request) throws Exception {
        log.info("=========用户列表=========");
        int pageSize = 10;
        int totalRecord = service.auditCount();
        int totalPage = 1;
        int offset = 1;
        PageModel pm = PageModel.newPageModel(pageSize, pageNum, totalRecord);
        if(totalRecord == 0){
            totalPage = 1;
            offset = 0;
        }else {
            totalPage = pm.getTotalPage();
            pm.setCurrentPage(pageNum);
            offset = pm.getOffset();
        }
        //待审核列表
        List<Account> list_1 = service.auditUser(pageSize, offset);
        for(int i = 0;i<list_1.size();i++){
            String s_utf8 = new String(list_1.get(i).getAddress(),"UTF-8");
            list_1.get(i).setAddress_utf8(s_utf8);
            Organ organ = service.selectOrganName(String.valueOf(list_1.get(i).getOrgan()));
            list_1.get(i).setOrganName(organ.getOrganName());
        }
        //用户列表
        List<Account> list_2 = service.auditUser_2(pageSize, offset);
        for(int i = 0;i<list_2.size();i++){
            String s_utf8 = new String(list_2.get(i).getAddress(),"UTF-8");
            list_2.get(i).setAddress_utf8(s_utf8);
            Organ organ = service.selectOrganName(String.valueOf(list_2.get(i).getOrgan()));
            list_2.get(i).setOrganName(organ.getOrganName());
        }
        model.addAttribute("list1",list_1);
        model.addAttribute("list2",list_2);
        model.addAttribute("totalPage",totalPage);
        if(totalRecord == 0){
            model.addAttribute("pageNum", 1);
        }else {
            model.addAttribute("pageNum", pm.getCurrentPage());
        }
        model.addAttribute("amount",totalRecord);
        return "affair";
    }

    @PostMapping("/user/auditSuccess")
    @ResponseBody
    public APIResponse auditSuccess(@RequestParam(value = "address") String address,HttpSession session,
            HttpServletRequest request) throws Exception {
        log.info("=========用户审核========="+address);
        APIResponse response = new APIResponse();
        boolean isTrue = service.auditUserRegisterSuccess(address);
        if(isTrue){
            Account account = service.loginByName_1(address);
            //查询机构名称
            Organ organ = service.selectOrganName(String.valueOf(account.getOrgan()));

            //广播上链
            Class.forName("org.sdk.util.Init");
            Transaction transaction = TransactionUtility.saveUser(Utility.getNonce("9cbf30db111483e4b84e77ca0e39378fd7605e1b"), HexBytes.fromHex("f00df601a78147ffe0b84de1dffbebed2a6ea965becd5d0bd7faf54f1f29c6b5"), address,account.getUsername(), account.getRole(), organ.getOrganName(), account.getOrgan());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode appleNode = objectMapper.convertValue(transaction, JsonNode.class);
            String result = HttpclientUtil.httpPostBody("http://192.168.1.167:7010/rpc/transaction",appleNode);
            JSONObject json_result = JSONObject.parseObject(result);
            if(json_result.getInteger("code") == 200){
                JSONArray json_data = (JSONArray) json_result.get("data");
                String hash = json_data.getString(0);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String date = df.format(new Date());
                boolean isAddHash = service.addHash(address.getBytes(),hash,1,date);


                response.setCode("200");
                response.setMsg("审核成功!");
            }

        }else{
            response.setCode("500");
            response.setMsg("审核失败!");
        }
        return response;
    }

    @PostMapping("/user/auditFail")
    @ResponseBody
    public APIResponse auditFail(@RequestParam(value = "address") String address,
                                    HttpServletRequest request){
        log.info("=========用户审核=========");
        APIResponse response = new APIResponse();
        boolean result = service.auditUserRegisterFail(address);
        if(result){
            response.setCode("200");
            response.setMsg("审核成功!");
        }else{
            response.setCode("500");
            response.setMsg("审核失败!");
        }
        return response;
    }

    @PostMapping("/user/guide_1")
    @ResponseBody
    public APIResponse guide_1(@RequestParam(value = "search") String search,
                            HttpServletRequest request){
        APIResponse response = new APIResponse();
        response.setCode("200");
        response.setMsg("审核失败!");
        return response;
    }

    @PostMapping("/user/guide_2")
    public String guide_2(
                        HttpServletRequest request){
        log.info("=========查询详情=========");
        return "guide";
    }

    @PostMapping("/user/detail_1")
    @ResponseBody
    public APIResponse detail_1(
            HttpServletRequest request){
        log.info("=========查询详情111=========");
        APIResponse response = new APIResponse();
        response.setCode("200");
        response.setMsg("审核失败!");
        return response;
    }

    @PostMapping("/user/detail")
    public String detail(
                        HttpServletRequest request){
        log.info("=========查询详情222=========");
        return "detail";
    }

    @PostMapping("/user/updateUser")
    public String updateUser(@RequestParam(value = "address") String address,
                             @RequestParam(value = "username") String username,
                             @RequestParam(value = "organName") int organ,
                             @RequestParam(value = "role") int role){
        boolean result = service.updateUser(username,role,organ,address);
//        if(result){
//            System.out.println("1");
//        }else {
//            System.out.println("2");
//        }
        return "affairpend";
    }
}