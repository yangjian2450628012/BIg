package tech.yobbo.genres.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xiaoJ on 2017/5/10.
 */
@Controller
@RequestMapping(value = "/genres")
public class GenresController {

    @RequestMapping
    public ModelAndView toGenresIndex(){
        ModelAndView modelAndView = new ModelAndView("genres/index");
        return modelAndView;
    }
}
