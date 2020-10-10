package com.tiankong44.controller.frontController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AboutMeController {
	@RequestMapping("/about")
	public String tag() {

		return "about";
	}
}
