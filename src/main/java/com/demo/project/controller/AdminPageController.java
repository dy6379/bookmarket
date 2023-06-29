package com.demo.project.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.project.entity.Page;
import com.demo.project.repository.PageRepository;

@Controller
@RequestMapping("/admin/pages")
public class AdminPageController {

	@Autowired
	private PageRepository pageRepo;

	@GetMapping
	public String page(Model model) {

		List<Page> pages = pageRepo.findAllByOrderBySortingAsc();

		model.addAttribute("pages", pages);

		return "admin/pages/page";
	}

	@GetMapping("/add")
	public String add(@ModelAttribute Page page) {
//		model.addAttribute("page", new Page());

		return "admin/pages/add";
	}

	@PostMapping("/add")
	public String add(@Valid @ModelAttribute Page page, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {
			return "admin/pages/add";
		}
		// 성공적으로 page 추가 되는 경우
		redirectAttributes.addFlashAttribute("message", "페이지 추가됨");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
				: page.getSlug().toLowerCase().replace(" ", "-");
		Page slugExists = pageRepo.findBySlug(slug);

		if (slugExists != null) {
			redirectAttributes.addFlashAttribute("message", "슬러그가 이미 있습니다. 다른것을 고르세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("page", page); // 입력내용을 보여주기위함
		} else {
			page.setSlug(slug); // 슬러그를 - 형식으로 수저정해 저장
			page.setSorting(100); // 기본 솔팅값

			pageRepo.save(page);
		}
		return "redirect:/admin/pages/add";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {
		Page page = pageRepo.findById(id).orElse(null);
		model.addAttribute("page", page);
		return "admin/pages/edit";
	}

	@PostMapping("/edit")
	public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "admin/pages/edit";
		}
		// 성공적으로 page 수정 되는 경우
		redirectAttributes.addFlashAttribute("message", "페이지 수정됨");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
				: page.getSlug().toLowerCase().replace(" ", "-");

		Page slugExists = pageRepo.findBySlugAndIdNot(slug, page.getId()); // 기존에 저장된 아이디의 슬러그는 제외하고 검사

		if (slugExists != null) {
			redirectAttributes.addFlashAttribute("message", "슬러그가 이미 있습니다. 다른것을 고르세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("page", page); // 입력내용을 보여주기위함
		} else {
			page.setSlug(slug); // 슬러그를 - 형식으로 수정해 저장
			page.setSorting(100); // 기본 솔팅값

			pageRepo.save(page);
		}
		return "redirect:/admin/pages/edit/" + page.getId(); // 다시 수정하던 edit/id 페이지로
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {

		pageRepo.deleteById(id);

		redirectAttributes.addFlashAttribute("message", "성공적으로 삭제되었습니다.");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/admin/pages";
	}

	@PostMapping("/reorder")
	public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {

	    int count = 1;
        Page page;

        for (int pageId : id) {
            Optional<Page> optionalPage = pageRepo.findById(pageId);
            page = optionalPage.get();
//        	page = pageRepo.findById(id);
            page.setSorting(count);
            pageRepo.save(page);
            count++;
        }

        return "ok";
    }
}
