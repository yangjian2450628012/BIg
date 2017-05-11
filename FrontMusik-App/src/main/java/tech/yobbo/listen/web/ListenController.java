package tech.yobbo.listen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xiaoJ on 2017/5/11.
 * Listten控制器
 */
@Controller
@RequestMapping(value = "listen")
public class ListenController {

    @RequestMapping
    public ModelAndView toListenIndex(){
        ModelAndView listenIndexView = new ModelAndView("listen/index");
        return listenIndexView;
    }
}
