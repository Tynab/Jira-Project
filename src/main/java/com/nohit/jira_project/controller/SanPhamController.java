package com.nohit.jira_project.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.nohit.jira_project.model.*;
import com.nohit.jira_project.service.*;
import com.nohit.jira_project.util.*;

import static com.nohit.jira_project.constant.AttributeConstant.*;
import static com.nohit.jira_project.constant.TemplateConstant.*;
import static com.nohit.jira_project.constant.ViewConstant.*;

@Controller
@RequestMapping(PRODUCT_VIEW)
public class SanPhamController {

    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Autowired
    private SanPhamService productService;

    // Fields
    private KhachHang mCurrentAccount;
    private SanPham mChoosenOne;
    private String mMsg;
    private boolean mIsByPass;
    private boolean mIsMsgShow;

    // Load product
    @GetMapping(value = {""})
    public ModelAndView product() {
        // All can go to pages: homepage/product/details/about/contact
        // User must login fisrt to go to pages cart and checkout
        var mav = new ModelAndView(PRODUCT_TEMP);
        mav.addObject(USER_PARAM, mCurrentAccount);
        mav.addObject(PRODUCTS_PARAM, productService.getDsSanPham());
        mIsByPass = false;
        return mav;
    }

    // Load top sale products
    @GetMapping(value = {"/sort"})
    public ModelAndView topsaleProduct(@RequestParam("sort") String criteria) {
        // All can go to pages: homepage/product/details/about/contact
        // User must login fisrt to go to pages cart and checkout
        var mav = new ModelAndView(PRODUCT_TEMP);
        mav.addObject(USER_PARAM, mCurrentAccount);
        if(criteria.equals("topSale")){
            mav.addObject(PRODUCTS_PARAM, productService.getDsSanPhamTopSale());
        } else if (criteria.equals("newOrder")){
            mav.addObject(PRODUCTS_PARAM, productService.getDsSanPhamNewestOrder());
        } else if (criteria.equals("ascendingPriceOrder")){
            mav.addObject(PRODUCTS_PARAM, productService.getDsSanPhamAscendingPriceOrder());
        } else if (criteria.equals("descendingPriceOrder")){
            mav.addObject(PRODUCTS_PARAM, productService.getDsSanPhamDescendingPriceOrder());
        } else{
            mav.addObject(PRODUCTS_PARAM, productService.getDsSanPham());
        }
        
        mIsByPass = false;
        return mav;
    }

    // Re-check choosen one
    private boolean isAliveChoosenOne() {
        // check the project has been declared
        if (mChoosenOne == null) {
            return false;
        } else {
            mChoosenOne = productService.getSanPham(mChoosenOne.getId());
            return mChoosenOne != null;
        }
    }

    // Check valid account
    private boolean isValidAccount() {
        // check bypass
        if (mIsByPass) {
            return true;
        } else {
            mCurrentAccount = authenticationUtil.getAccount();
            return mCurrentAccount != null;
        }
    }

    // Show message
    private void showMessageBox(ModelAndView mav) {
        // check flag
        if (mIsMsgShow) {
            mav.addObject(FLAG_MSG_PARAM, true);
            mav.addObject(MSG_PARAM, mMsg);
            mIsMsgShow = false;
        }
    }
}
