package com.lottery.project.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lottery.project.model.Account;
import com.lottery.project.model.Lottery;
import com.lottery.project.service.AccountService;
import com.lottery.project.service.LotteryService;

@Controller
public class LotteryController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private LotteryService lotteryService;

	@GetMapping(value = "/home")
	public String showHome(Model model) {
		Lottery lastMaxLottery = lotteryService.getLastestMax();
		Lottery lastMegaLottery = lotteryService.getLastestMega();
		model.addAttribute("max", lastMaxLottery);
		model.addAttribute("mega",lastMegaLottery);
		return "home";
	}

	@GetMapping(value = "/header")
	public String showHeader() {
		return "header";
	}

	@GetMapping(value = "/footer")
	public String showFooter() {
		return "footer";
	}

	@GetMapping(value = "/homebody")
	public String showHBody() {
		return "homebody";
	}

	

	@GetMapping(value = "/checkresult")
	public String showCheck(Model model) {
		List<Lottery> list = lotteryService.getMaxLottery();
		model.addAttribute("list",list);
		return "checkresult";
	}

	@GetMapping(value = "/checkresultmega")
	public String showCheckMega(Model model) {
		List<Lottery> list = lotteryService.getMegaLottery();
		model.addAttribute("list", list);
		return "checkresult-mega";
	}

	@GetMapping(value = "/lotterymanage")
	public String showLotteryManage(Model model) {
		return lotteryPagination(1,model);
		
	}

	

	@GetMapping(value = "/addlotterymax")
	public String addMax(Model model) {
		Lottery lottery = new Lottery();
		model.addAttribute("lottery", lottery);
		return "add-lotterymax";
	}

	@GetMapping(value="/addlotterymega")
	public String addMega(Model model){
		Lottery lottery = new Lottery();
		model.addAttribute("lottery", lottery);
		return "add-lotterymega";
	}

	@PostMapping(value="/addMax")
	public String addMaxLottery(@ModelAttribute (value = "lottery") Lottery lottery, @RequestParam (value="special-price1") String special1,
			@RequestParam (value="special-price2") String special2, @RequestParam (value="first-price1") String first1, 
			@RequestParam (value="first-price2") String first2, @RequestParam (value="first-price3") String first3,
			@RequestParam (value="first-price4") String first4, @RequestParam (value="second-price1") String second1,
			@RequestParam (value="second-price2") String second2, @RequestParam (value="second-price3") String second3,
			@RequestParam (value="second-price4") String second4, @RequestParam (value="second-price5") String second5,
			@RequestParam (value="second-price6") String second6, @RequestParam (value="third-price1") String third1,
			@RequestParam (value="third-price2") String third2, @RequestParam (value="third-price3") String third3,
			@RequestParam (value="third-price4") String third4, @RequestParam (value="third-price5") String third5,
			@RequestParam (value="third-price6") String third6, @RequestParam (value="third-price7") String third7,
			@RequestParam (value="third-price8") String third8, @RequestParam (value = "drawDate") Date date, RedirectAttributes redirectAttributes){
		if(lotteryService.validateDateMax(date)) {
		String special = String.join(", ",special1, special2);
		String first = String.join(", ", first1, first2, first3, first4);
		String second = String.join(", ", second1, second2, second3, second4, second5, second6);
		String third = String.join(", ", third1, third2, third3, third4, third5, third6, third7, third8);
		lottery.setFirstPrice(first);
		lottery.setSpecialPrice(special);
		lottery.setSecondPrice(second);
		lottery.setThirdPrice(third);
		lotteryService.saveLottery(lottery);
		}else {
			String error = "Date already exist or before today!";
			redirectAttributes.addFlashAttribute("error", error);
			return "redirect:/addlotterymax";
		}
		 return "redirect:/lotterymanage";
	}
	@PostMapping(value="/addMega")
	public String addMegaLottery(@ModelAttribute (value="lottery") Lottery lottery, @RequestParam (value = "check-mega1") String check1,
			@RequestParam (value = "check-mega2") String check2, @RequestParam (value = "check-mega3") String check3,
			@RequestParam (value = "check-mega4") String check4, @RequestParam (value = "check-mega5") String check5,
			@RequestParam (value = "check-mega1") String check6, @RequestParam (value = "drawDate") Date date, RedirectAttributes redirectAttributes) {
		String[] numberArr = {check1, check2, check3, check4, check5, check6};
		if(lotteryService.validateMega(numberArr) && lotteryService.validateDateMega(date)) {
		String winning = String.join(", ", check1, check2, check3, check4, check5, check6);
		lottery.setWinNumber(winning);
		lotteryService.saveLottery(lottery);
		} else {
			String error="";
			if(!(lotteryService.validateMega(numberArr) && lotteryService.validateDateMega(date))) {
				error = "Invalid input: Each number must be different and must be from 1-45! Date already exist or before today!";
			} else if(!lotteryService.validateMega(numberArr)) {
				error = "Invalid input: Each number must be different and must be from 1-45!";
			} else {
				error= "Date already exist or before today!";
			}
			redirectAttributes.addFlashAttribute("error", error);
			return "redirect:/addlotterymega";
		}
		return "redirect:/lotterymanage";
		
		
	}
	
	@GetMapping(value="/updatelottery/max/{id}")
	public String updateMax(@PathVariable(value="id") long id, Model model) {
		model.addAttribute("lottery",lotteryService.getLotteryByID(id));
		return "lottery-updatemax";
		
	}
	@GetMapping(value="/updatelottery/mega/{id}")
	public String updateMega(@PathVariable(value="id") long id, Model model) {
		model.addAttribute("lottery",lotteryService.getLotteryByID(id));
		return "lottery-updatemega";
		
	}
	@GetMapping(value="/lotterymanage/{pageNo}")
	public String lotteryPagination(@PathVariable (value="pageNo") int pageNo, Model model) {
		int pageSize = 10;
		Page<Lottery> page = lotteryService.findPaginated(pageNo, pageSize);
		List<Lottery> list = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("listLottery", list);
		return "lottery-management";
		
	}
	
	
	
	@PostMapping(value="/checkmax")
	public String checkMax(@RequestParam(value="check") String checkNumber, @RequestParam(value="date") Date drawDate, RedirectAttributes redirectAttributes) {
		Lottery lottery = lotteryService.findByDate(drawDate, "max");
		String result = lotteryService.checkMax(checkNumber, lottery);
		redirectAttributes.addFlashAttribute("result",result);
		return "redirect:/checkresult";
	}
	
	@GetMapping(value="/deletelottery/{id}")
	public String deleteLottery(@PathVariable (value="id") long id) {
		lotteryService.deleteLottery(id);
		return "redirect:/lotterymanage";
	}
	
	@PostMapping(value="/checkmega")
	public String checkMega(@RequestParam (value="check-mega1") String check1,
			@RequestParam (value="check-mega2") String check2,
			@RequestParam (value="check-mega3") String check3,
			@RequestParam (value="check-mega4") String check4,
			@RequestParam (value="check-mega5") String check5,
			@RequestParam (value="check-mega6") String check6,
			@RequestParam (value="date") Date date,
			RedirectAttributes redirectAttributes) {
		String [] arr= {check1, check2, check3, check4, check5, check6};
		if(lotteryService.validateMega(arr)) {
			Lottery lottery= lotteryService.findByDate(date, "mega");
			String result = lotteryService.checkMega(arr, lottery);
			redirectAttributes.addFlashAttribute("result", result);
		}else {
			String error = "Invalid input: Each number must be different and must be from 1-45!";
			redirectAttributes.addFlashAttribute("error", error);
		}
		return "redirect:/checkresultmega";
	}
	@PostMapping(value="/deletemultilottery")
	public String deleteMulLottery(HttpServletRequest request) {
		for(String i: request.getParameterValues("id")) {
			long id = Long.parseLong(i);
			lotteryService.deleteLottery(id);
		}
		return "redirect:/lotterymanage";
	}
	@PostMapping(value="/searchdate")
	public String searchDate(@RequestParam (value = "search") Date search, Model model) {
		List<Lottery> lottery = lotteryService.getAllLottery();
		List<Lottery> dateList = new ArrayList<Lottery>();
		for(Lottery a:lottery) {
			if(a.getDrawDate().equals(search)) {
				dateList.add(a);
			}
		}
		model.addAttribute("listLottery", dateList);
		return "lottery-management";	
	}
	
}
