package com.demo.project.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.project.entity.Book;
import com.demo.project.entity.Cart;
import com.demo.project.entity.Product;
import com.demo.project.repository.BookRepository;
import com.demo.project.repository.ProductRepository;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private BookRepository bookRepo;

	@GetMapping("/add/{id}")
	public String add(@PathVariable int id, HttpSession session, Model model, 
			@RequestParam(required = false) String cartPage) {

	    Product product = null;
	    Book book = null;

	    if (id < 1000) {
	        book = bookRepo.findById(id);
	    } else if (id >= 1000) {
	        product = productRepo.findById(id);
	    }

	    if (session.getAttribute("cart") == null) {
	        HashMap<Integer, Cart> cart = new HashMap<>();
	        if (book != null) {
	            cart.put(id, new Cart(id, book.getName(), book.getPrice(), 1, book.getImage()));
	        } else if (product != null) {
	            cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
	        }
	        session.setAttribute("cart", cart);
	    } else {
	        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
	        if (book != null) {
	            if (cart.containsKey(id)) {
	                int qty = cart.get(id).getQuantity();
	                cart.put(id, new Cart(id, book.getName(), book.getPrice(), ++qty, book.getImage()));
	            } else {
	                cart.put(id, new Cart(id, book.getName(), book.getPrice(), 1, book.getImage()));
	                session.setAttribute("cart", cart);
	            }
	        } else if (product != null) {
	            if (cart.containsKey(id)) {
	                int qty = cart.get(id).getQuantity();
	                cart.put(id, new Cart(id, product.getName(), product.getPrice(), ++qty, product.getImage()));
	            } else {
	                cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
	                session.setAttribute("cart", cart);
	            }
	        }
	    }

	    HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

	    int size = 0;
	    int total = 0;

	    for (Cart item : cart.values()) {
	        size += item.getQuantity();
	        total += item.getQuantity() * Integer.parseInt(item.getPrice());
	    }

	    model.addAttribute("size", size);
	    model.addAttribute("total", total);

	    if (cartPage != null) {
	        return "redirect:/cart/view";
	    }

	    return "cart_view";
	}

	
	@GetMapping("/view")
	public String cartView(Model model, HttpSession session) {
		if (session.getAttribute("cart") == null) {
			return "redirect:/";
		}

		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
		model.addAttribute("cart",cart);
		model.addAttribute("noCartView", true);
		return "cart";
	}
	
	// - 버튼
	@GetMapping("/subtract/{id}")
	public String subtract(@PathVariable int id, HttpSession session, Model model,
						HttpServletRequest httpServletRequest) {
		
		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

		int qty = cart.get(id).getQuantity();
		if (qty == 1) {
			cart.remove(id);
			if (cart.size() == 0) {
				session.removeAttribute("cart");
			}
		} else {
			cart.get(id).setQuantity(--qty);
		}

		String refererLink = httpServletRequest.getHeader("Referer"); // 요청된 이전 주소 (이전 페이지 정보가 전부 들어있음 파라메터등 )

		return "redirect:" + refererLink; // 다시 그전 페이지로 돌아감
	}
	
	// 삭제
	@GetMapping("/remove/{id}")
	public String remove(@PathVariable int id, HttpSession session, Model model,
			HttpServletRequest httpServletRequest) {

		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

		cart.remove(id);
		if (cart.size() == 0) {
			session.removeAttribute("cart");
		}

		String refererLink = httpServletRequest.getHeader("Referer"); // 요청된 이전 주소 (이전 페이지 정보가 전부 들어있음 파라메터등 )

		return "redirect:" + refererLink; // 다시 그전 페이지로 돌아감
	}
	
	// 비우기
	@GetMapping("/clear")
	public String clear(HttpSession session, HttpServletRequest httpServletRequest) {

		session.removeAttribute("cart");
		
		String refererLink = httpServletRequest.getHeader("Referer"); // 요청된 이전 주소 (이전 페이지 정보가 전부 들어있음 파라메터등 )

		return "redirect:" + refererLink; // 다시 그전 페이지로 돌아감
	}
	

}
