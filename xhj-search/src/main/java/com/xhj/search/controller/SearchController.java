package com.xhj.search.controller;

import com.xhj.search.service.MallSearchService;
import com.xhj.search.vo.SearchParam;
import com.xhj.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String ListPage(SearchParam searchParam, Model model){
        SearchResult search = mallSearchService.search(searchParam);
        model.addAttribute("result",search);
        System.out.println(search);
        return "list";
    }
//
//    public static void main(String[] args) {
//        // LinkedList 双向链表
//        Node jack = new Node("jack");
//        Node sss = new Node("sss");
//        Node ddd = new Node("ddd");
//        // firse
//        jack.next = sss;
//        sss.next = ddd;
//        // last
//        ddd.perx = sss;
//        sss.perx = jack;
//
//        Node firse = jack;
//        Node last = ddd;
//
//        while (true){
//            if (firse == null){
//                break;
//            }
//            System.out.println(firse);
//            firse = firse.next;
//        }
//        while (true){
//            if (last == null){
//                break;
//            }
//            System.out.println(last);
//            last = last.perx;
//        }
//
//    }
//
//}
//
//class Node{
//    public Object item;
//    public Node next;
//    public Node perx;
//
//    public Node(Object name){
//        this.item = name;
//    }
//
//    @Override
//    public String toString() {
//        return "Node{" +
//                "item=" + item +
//                '}';
//    }
}
