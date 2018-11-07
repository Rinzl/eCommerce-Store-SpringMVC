package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.StoreUtils.UserRole;
import com.thd.ecommercespringmvc.StoreUtils.Utilities;
import com.thd.ecommercespringmvc.model.*;
import com.thd.ecommercespringmvc.storageservices.ImageStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    private static Logger logger = LogManager.getLogger(AdminController.class);

    private final ImageStorage imageStorage;

    @Autowired
    public AdminController(ImageStorage imageStorage) {
        this.imageStorage = imageStorage;
    }

    @GetMapping(value = "/adminLogin")
    public String adminLogin(HttpSession httpSession, Model model) {
        if (!checkAdminDetail(httpSession)) {
            return "redirect:/admin";
        }
        model.addAttribute("adminTemp", new User());
        return "/admin/login";
    }
    @GetMapping(value = "admin/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("admin");
        return "redirect:/adminLogin";
    }
    @PostMapping(value = "/admin/login")
    public String login(HttpSession httpSession, Model model, @ModelAttribute User user) {
        logger.info("Attempt login : " + user);
        User validUser = Application.database.getAdminInfo(user.getUserName(), user.getPassword());
        if(validUser != null) {
            logger.info("Admin has logged in : " +validUser);
            httpSession.setAttribute("admin", validUser);
            return "redirect:/admin";
        }
        else {
            logger.info("Wrong username or password !");
            model.addAttribute("loginFailed", true);
            model.addAttribute("adminTemp", new User());
            return "/admin/login";
        }
    }

    @GetMapping(value = {"/admin", "dashboard"})
    public String dashboard(HttpSession httpSession, Model model) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        model.addAttribute("numberOfNewBill", Application.database.getNewBillToDay());
        model.addAttribute("billStatus", Application.database.getBillStatus());
        model.addAttribute("newCustomer", Application.database.getNewCustomerNumber());
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("numberOfnewSale", Application.database.getNewSaleToday());
        return "admin/dashboard";
    }
    private boolean checkAdminDetail(HttpSession httpSession) {
        User admin = (User) httpSession.getAttribute("admin");
        return admin == null;
    }

    @PostMapping(value = "/admin/products/add")
    public String addProduct(HttpSession httpSession, @ModelAttribute ProductFormUpload product, Model model) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        logger.info("A new product is created !");
        Product p = Utilities.convertRawProduct(product);
        String fileName = imageStorage.store(product.getMultipartFile());
        p.setImage(Product.PRODUCT_RELATIVE_IMG_PATH+fileName);
        logger.info(p);
        int kq = Application.database.createProduct(p);
        model.addAttribute("product", new ProductFormUpload());
        if (kq == 0) {
            model.addAttribute("notif", false);
        } else {
            model.addAttribute("notif", true);
        }
        model.addAttribute("categories", Application.database.getCategoryList());
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        return "admin/product-add";
    }

    @GetMapping(value = "/admin/products/add")
    public String addProduct(HttpSession httpSession, Model model) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        model.addAttribute("categories", Application.database.getCategoryList());
        model.addAttribute("product", new ProductFormUpload());
        model.addAttribute("notif", null);
        return "admin/product-add";
    }

    @GetMapping(value = "/admin/products/edit", params = "id")
    public String editProduct(HttpSession httpSession, Model model, @RequestParam int id) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        Product product = Application.database.getProduct(id);
        ProductFormUpload pfu = new ProductFormUpload();
        pfu.setImage(product.getImage());
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        model.addAttribute("categories", Application.database.getCategoryList());
        model.addAttribute("product", product);
        model.addAttribute("productForm", pfu);
        model.addAttribute("notif", null);
        return "admin/product-edit";
    }
    @PostMapping(value = "/admin/products/edit", params = "id")
    public String editProduct(HttpSession httpSession, @RequestParam int id, @ModelAttribute ProductFormUpload product, Model model) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        logger.info("update request : " + product);
        Product p = Utilities.convertRawProduct(product);
        p.setId(id);
        String fileName = imageStorage.store(product.getMultipartFile());
        boolean isImage = false;
        if (fileName != null) {
            p.setImage(Product.PRODUCT_RELATIVE_IMG_PATH+fileName);
            isImage = true;
        }
        int kq = Application.database.updateProduct(p, isImage);
        if (kq != 0) {
            model.addAttribute("notif", true);
            logger.info("Product is updated !");
            logger.info(p);
        } else {
            model.addAttribute("notif", false);
            logger.error("Product is not updated");
            model.addAttribute("admin", httpSession.getAttribute("admin"));
            return "product-edit";
        }
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        model.addAttribute("products", Application.database.getAllProduct());
        return "admin/product-overview";
    }

    @PostMapping(value = "/admin/products/delete")
    @ResponseBody
    public String deleteProduct(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        int productID = Utilities.getIdDeleteOrAdd(request);
        JSONObject response = new JSONObject();
        if (Application.database.deleteProduct(productID)) {
            response.put("status",1);
            return response.toJSONString();
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }

    @GetMapping(value = "/admin/products/overview")
    public String productOverview(HttpSession httpSession, Model model) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        List<Product> products = Application.database.getAllProduct();
        model.addAttribute("products", products);
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        model.addAttribute("notif", null);
        return "admin/product-overview";
    }
    @GetMapping(value = "/admin/products/category")
    public String categoryManage(HttpSession httpSession, Model model) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        model.addAttribute("categories", Application.database.getCategoryList());
        return "admin/product-category";
    }

    @PostMapping(value = "/admin/products/category/add")
    @ResponseBody
    public String addCategory(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        Category c = Utilities.getCategoryFromJSON(request);
        JSONObject response = new JSONObject();
        if (c!= null) {
            Application.database.insertCategory(c);
            logger.info("new category is created : " + c);
            response.put("status",1);
            return response.toJSONString();
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
    @PostMapping(value = "/admin/products/category/edit")
    @ResponseBody
    public String editCategory(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        Category c = Utilities.getCategoryFromJSON(request);
        JSONObject response = new JSONObject();
        if (c!= null) {
            Application.database.updateCategory(c);
            logger.info("category is updateted : " + c);
            response.put("status",1);
            return response.toJSONString();
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
    @PostMapping(value = "/admin/products/category/delete")
    @ResponseBody
    public String deleteCategory(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        Category c = Utilities.getCategoryFromJSON(request);
        JSONObject response = new JSONObject();
        if (c!= null && Application.database.deleteCategory(c.getId())) {
            logger.info("category is deleted : " + c);
            response.put("status",1);
            return response.toJSONString();
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
    @GetMapping(value = "/admin/bills/overview")
    public String billManage(HttpSession httpSession, Model model) {
        if (checkAdminDetail(httpSession)) {
            return "redirect:/adminLogin";
        }
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        model.addAttribute("bills", Application.database.billList());
        return "admin/bills-overview";
    }
    @PostMapping(value = "/admin/bills/delete")
    @ResponseBody
    public String deleteBill(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        int billID = Utilities.getIdDeleteOrAdd(request);
        JSONObject response = new JSONObject();
        if (Application.database.deleteBill(billID)) {
            logger.info("Bill is deleted : " + billID);
            response.put("status",1);
            return response.toJSONString();
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
    @PostMapping(value = "/admin/bills/edit")
    @ResponseBody
    public String editBill(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        JSONObject response = new JSONObject();
        logger.info("Bill update request : " + request);
        Bill bill = Utilities.getBillFromJSON(request);
        if (bill!= null) {
            Application.database.updateBill(bill);
            logger.info("bill is updateted : " + bill.getId());
            response.put("status",1);
            return response.toJSONString();
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
    @GetMapping(value = "/admin/dashboardInfo")
    @ResponseBody
    public String getCustomerNumber(HttpSession httpSession) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        JSONObject object = new JSONObject();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Map<Integer, Integer> map = Application.database.getBillStatus();
        object.put("billStatus", map);
        object.put("monthlySaleThisYear", Application.database.monthlySaleNumber(year));
        object.put("monthlySaleLastYear", Application.database.monthlySaleNumber(year-1));
        object.put("salePerCategory", Application.database.salePerCategory(month, year));
        return object.toJSONString();
    }
    @GetMapping(value = "/admin/employee/overview")
    public String employeeOverview(HttpSession httpSession, Model model) {
        User admin = (User) httpSession.getAttribute("admin");
        if (checkAdminDetail(httpSession) || admin.getRole() != UserRole.ADMIN) {
            return "redirect:/admin";
        }
        model.addAttribute("employee", Application.database.getEmpList());
        model.addAttribute("admin", httpSession.getAttribute("admin"));
        return "admin/employee-overview";
    }
    @PostMapping(value = "/admin/employee/add")
    @ResponseBody
    public String addEmployee(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        logger.info("Add empl request : " + request);
        User user = Utilities.getUserFromJSON(request);
        JSONObject response = new JSONObject();
        if (user!= null) {
            boolean isExist = Application.database.isUsernameExist(user.getUserName().trim());
            if (!isExist) {
                Application.database.createUser(user);
                logger.info("new admin is created : " + user);
                response.put("status",1);
                return response.toJSONString();
            } else {
                response.put("status", 2);
                return response.toJSONString();
            }
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
    @PostMapping(value = "/admin/employee/delete")
    @ResponseBody
    public String deleteEmp(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        int userID = Utilities.getIdDeleteOrAdd(request);
        JSONObject response = new JSONObject();
        if (Application.database.deleteUser(userID)) {
            logger.info("Emp is deleted : " + userID);
            response.put("status",1);
            return response.toJSONString();
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
    @PostMapping(value = "/admin/employee/edit")
    @ResponseBody
    public String editEmpl(HttpSession httpSession, @RequestBody String request) {
        if (checkAdminDetail(httpSession)) {
            return "NULL";
        }
        JSONObject response = new JSONObject();
        logger.info("User update request : " + request);
        User user = Utilities.getUserFromJSON(request);
        if (user != null) {
            boolean isExist = Application.database.isUsernameExist(user.getUserName().trim());
            if (!isExist) {
                Application.database.updateUser(user);
                logger.info("User is updateted : " + user.getId());
                response.put("status", 1);
                return response.toJSONString();
            } else {
                response.put("status", 2);
                return response.toJSONString();
            }
        } else {
            response.put("status", 0);
            return response.toJSONString();
        }
    }
}
