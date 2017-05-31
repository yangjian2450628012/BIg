package tech.yobbo.genres.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xiaoJ on 2017/5/10.
 */
@Controller
@RequestMapping(value = "/genres")
public class GenresController {
    private static final Logger log = LoggerFactory.getLogger(GenresController.class);

    @RequestMapping
    public ModelAndView toGenresIndex(){
        ModelAndView modelAndView = new ModelAndView("genres/index");
        log.debug("进来了。。。。。。。");
        return modelAndView;
    }
}
