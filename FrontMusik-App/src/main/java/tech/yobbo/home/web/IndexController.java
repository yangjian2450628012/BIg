package tech.yobbo.home.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tech.yobbo.home.dao.IndexDao;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/index")
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	private IndexDao indexDao;

	/**
	 * 跳转到首页
	 * @return
	 */
	@RequestMapping
	public ModelAndView toIndex(){
		ModelAndView indexView = new ModelAndView("home/index");
		//查询列表数据
//		indexView.addObject("name","xiaoyang");
		return indexView;
	}

	@RequestMapping("queryAll")
	@ResponseBody
	public List<Map<String,String>> queryAll(){
		List<Map<String,String>> list = this.indexDao.queryAll();
		System.out.println(list.size());
		return this.indexDao.queryAll();
	}
	
	@RequestMapping("query")
	public ModelAndView query(){
		logger.info("query => 跳转路径");
		ModelAndView view = new ModelAndView("detail");
		view.addObject("name", "xiaoyang");
		return view;
	}

	@Autowired
	public void setIndexDao(IndexDao indexDao) {
		this.indexDao = indexDao;
	}

}
