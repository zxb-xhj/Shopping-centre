package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.search.service.MallSearchService;
import com.atguigu.gulimall.search.service.ipml.MallSearchServiceIpml;
import com.atguigu.gulimall.search.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @Author: xhj
 * @Date: 2023/05/05/10:53
 * @Description:
 */
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String ListPage(SearchParam searchParam){
        mallSearchService.search(searchParam);
        System.out.println("list");
        return "list";
    }

    public static void main(String[] args) {
        // LinkedList 双向链表
        Node jack = new Node("jack");
        Node sss = new Node("sss");
        Node ddd = new Node("ddd");
        // firse
        jack.next = sss;
        sss.next = ddd;
        // last
        ddd.perx = sss;
        sss.perx = jack;

        Node firse = jack;
        Node last = ddd;

        while (true){
            if (firse == null){
                break;
            }
            System.out.println(firse);
            firse = firse.next;
        }
        while (true){
            if (last == null){
                break;
            }
            System.out.println(last);
            last = last.perx;
        }

    }

}

class Node{
    public Object item;
    public Node next;
    public Node perx;

    public Node(Object name){
        this.item = name;
    }

    @Override
    public String toString() {
        return "Node{" +
                "item=" + item +
                '}';
    }
}
