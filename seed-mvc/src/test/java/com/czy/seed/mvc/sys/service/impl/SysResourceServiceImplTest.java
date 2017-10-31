//package com.czy.seed.mvc.sys.service.impl;
//
//import com.czy.seed.mvc.sys.entity.SysResource;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by PLC on 2017/5/30.
// */
//public class SysResourceServiceImplTest {
//    @Ignore
//    public void buildResourceTree() throws Exception {
//        List<SysResource> sysResourceList = new ArrayList<SysResource>();
//        sysResourceList.add(new SysResource(1L, 3L));
//        sysResourceList.add(new SysResource(1L, 4L));
//        sysResourceList.add(new SysResource(2L, 5L));
//        sysResourceList.add(new SysResource(2L, 6L));
//        sysResourceList.add(new SysResource(2L, 5L));
//        sysResourceList.add(new SysResource(2L, 7L));
//        sysResourceList.add(new SysResource(3L, 8L));
//        sysResourceList.add(new SysResource(4L, 9L));
//        sysResourceList.add(new SysResource(0L, 1L));
//        sysResourceList.add(new SysResource(0L, 2L));
//        SysResourceServiceImpl sysResourceService = new SysResourceServiceImpl();
//        List<SysResource> sysResources = sysResourceService.buildTree(sysResourceList);
//        System.out.println(sysResources);
//    }
//
//    @Ignore
//    public void findResourceForLoginUser() throws Exception {
//        List<SysResource> sysResourceList = new ArrayList<SysResource>();
////        sysResourceList.add(new SysResource(0L, 1L));
//        sysResourceList.add(new SysResource(1L, 3L));
//        sysResourceList.add(new SysResource(1L, 4L));
//        sysResourceList.add(new SysResource(2L, 5L));
//        sysResourceList.add(new SysResource(2L, 6L));
//        sysResourceList.add(new SysResource(2L, 5L));
//        sysResourceList.add(new SysResource(2L, 7L));
//        sysResourceList.add(new SysResource(3L, 8L));
//        sysResourceList.add(new SysResource(4L, 9L));
//
//
//        List<SysResource> rootSysResource = new ArrayList<SysResource>();
//        rootSysResource.add(new SysResource(0L, 1L));
//        rootSysResource.add(new SysResource(0L, 2L));
//
//        SysResourceServiceImpl sysResourceService = new SysResourceServiceImpl();
//        sysResourceService.findChildrenResource(sysResourceList,rootSysResource);
//        System.out.println(rootSysResource);
//    }
//
//}