package com.lottery.project.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lottery.project.model.Account;
import com.lottery.project.model.CheckHistory;
import com.lottery.project.model.Lottery;
import com.lottery.project.service.CheckHistoryService;
import com.lottery.project.service.LotteryService;

@Controller
public class LotteryController {
	@Autowired
	private LotteryService lotteryService;
	@Autowired
	private CheckHistoryService checkHistoryService;

	@GetMapping(value = "/home")
	public String showHome(Model model) {
		Lottery lastMaxLottery = lotteryService.getLastestMax();
		Lottery lastMegaLottery = lotteryService.getLastestMega();
		model.addAttribute("max", lastMaxLottery);
		model.addAttribute("mega", lastMegaLottery);
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
		model.addAttribute("list", list);
		return "checkresult";
	}

	@GetMapping(value = "/checkresultmega")
	public String showCheckMega(Model model) {
		List<Lottery> list = lotteryService.getMegaLottery();
		model.addAttribute("list", list);
		return "checkresult-mega";
	}

	@GetMapping(value = "/lotterymanage")
	public String showLotteryManage(Model model, HttpSession session) {
		return lotteryPagination(1, model, session);

	}

	@GetMapping(value = "/addlotterymax")
	public String addMax(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				Lottery lottery = new Lottery();
				model.addAttribute("lottery", lottery);
				return "add-lotterymax";
			}
		}
		return "redirect:/home";
	}

	@GetMapping(value = "/addlotterymega")
	public String addMega(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				Lottery lottery = new Lottery();
				model.addAttribute("lottery", lottery);
				return "add-lotterymega";
			}
		}
		return "redirect:/home";
	}

	@PostMapping(value = "/addMax")
	public String addMaxLottery(@ModelAttribute(value = "lottery") Lottery lottery,
			@RequestParam(value = "special-price1") String special1,
			@RequestParam(value = "special-price2") String special2,
			@RequestParam(value = "first-price1") String first1, @RequestParam(value = "first-price2") String first2,
			@RequestParam(value = "first-price3") String first3, @RequestParam(value = "first-price4") String first4,
			@RequestParam(value = "second-price1") String second1,
			@RequestParam(value = "second-price2") String second2,
			@RequestParam(value = "second-price3") String second3,
			@RequestParam(value = "second-price4") String second4,
			@RequestParam(value = "second-price5") String second5,
			@RequestParam(value = "second-price6") String second6, @RequestParam(value = "third-price1") String third1,
			@RequestParam(value = "third-price2") String third2, @RequestParam(value = "third-price3") String third3,
			@RequestParam(value = "third-price4") String third4, @RequestParam(value = "third-price5") String third5,
			@RequestParam(value = "third-price6") String third6, @RequestParam(value = "third-price7") String third7,
			@RequestParam(value = "third-price8") String third8, @RequestParam(value = "drawDate") Date date,
			RedirectAttributes redirectAttributes, Model model) {
		if (lotteryService.validateDateMax(date)) {
			String special = String.join(", ", special1, special2);
			String first = String.join(", ", first1, first2, first3, first4);
			String second = String.join(", ", second1, second2, second3, second4, second5, second6);
			String third = String.join(", ", third1, third2, third3, third4, third5, third6, third7, third8);
			String[] specialArr = special.split(", ");
			String[] firstArr = first.split(", ");
			String[] secondArr = second.split(", ");
			String[] thirdArr = third.split(", ");
			if (lotteryService.validateMax(thirdArr) && lotteryService.validateMax(specialArr)
					&& lotteryService.validateMax(firstArr) && lotteryService.validateMax(secondArr)) {
				lottery.setFirstPrice(first);
				lottery.setSpecialPrice(special);
				lottery.setSecondPrice(second);
				lottery.setThirdPrice(third);
				lotteryService.saveLottery(lottery);
			} else {
				String error = "Each price must have different numbers!";
				model.addAttribute("error", error);
				model.addAttribute("special1", special1);
				model.addAttribute("special2", special2);
				model.addAttribute("first1", first1);
				model.addAttribute("first2", first2);
				model.addAttribute("first3", first3);
				model.addAttribute("first4", first4);
				model.addAttribute("second1", second1);
				model.addAttribute("second2", second2);
				model.addAttribute("second3", second3);
				model.addAttribute("second4", second4);
				model.addAttribute("second5", second5);
				model.addAttribute("second6", second6);
				model.addAttribute("third1", third1);
				model.addAttribute("third2", third2);
				model.addAttribute("third3", third3);
				model.addAttribute("third4", third4);
				model.addAttribute("third5", third5);
				model.addAttribute("third6", third6);
				model.addAttribute("third7", third7);
				model.addAttribute("third8", third8);
				return "add-lotterymax";
			}
		} else {
			String error = "Date already exist or before today!";
			model.addAttribute("error", error);
			model.addAttribute("special1", special1);
			model.addAttribute("special2", special2);
			model.addAttribute("first1", first1);
			model.addAttribute("first2", first2);
			model.addAttribute("first3", first3);
			model.addAttribute("first4", first4);
			model.addAttribute("second1", second1);
			model.addAttribute("second2", second2);
			model.addAttribute("second3", second3);
			model.addAttribute("second4", second4);
			model.addAttribute("second5", second5);
			model.addAttribute("second6", second6);
			model.addAttribute("third1", third1);
			model.addAttribute("third2", third2);
			model.addAttribute("third3", third3);
			model.addAttribute("third4", third4);
			model.addAttribute("third5", third5);
			model.addAttribute("third6", third6);
			model.addAttribute("third7", third7);
			model.addAttribute("third8", third8);
			return "add-lotterymax";
		}
		return "redirect:/lotterymanage";
	}

	@PostMapping(value = "/addMega")
	public String addMegaLottery(@ModelAttribute(value = "lottery") Lottery lottery,
			@RequestParam(value = "check-mega1") String check1, @RequestParam(value = "check-mega2") String check2,
			@RequestParam(value = "check-mega3") String check3, @RequestParam(value = "check-mega4") String check4,
			@RequestParam(value = "check-mega5") String check5, @RequestParam(value = "check-mega6") String check6,
			@RequestParam(value = "drawDate") Date date, Model model) {
		String[] numberArr = { check1, check2, check3, check4, check5, check6 };
		if (lotteryService.validateMega(numberArr) && lotteryService.validateDateMega(date)) {
			String winning = String.join(", ", check1, check2, check3, check4, check5, check6);
			lottery.setWinNumber(winning);
			lotteryService.saveLottery(lottery);
		} else {
			String error = "";
			if (!(lotteryService.validateMega(numberArr) && lotteryService.validateDateMega(date))) {
				error = "Invalid input: Each number must be different and must be from 1-45! Date already exist or before today!";
			} else if (!lotteryService.validateMega(numberArr)) {
				error = "Invalid input: Each number must be different and must be from 1-45!";
			} else {
				error = "Date already exist or before today!";
			}
			model.addAttribute("error", error);
			model.addAttribute("checkmega1", check1);
			model.addAttribute("checkmega2", check2);
			model.addAttribute("checkmega3", check3);
			model.addAttribute("checkmega4", check4);
			model.addAttribute("checkmega5", check5);
			model.addAttribute("checkmega6", check6);
			return "add-lotterymega";
		}
		return "redirect:/lotterymanage";

	}

	@GetMapping(value = "/updatelottery/max/{id}")
	public String updateMax(@PathVariable(value = "id") long id, Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				model.addAttribute("lottery", lotteryService.getLotteryByID(id));
				return "lottery-updatemax";
			}
		}
		return "redirect:/home";

	}

	@GetMapping(value = "/updatelottery/mega/{id}")
	public String updateMega(@PathVariable(value = "id") long id, Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				model.addAttribute("lottery", lotteryService.getLotteryByID(id));
				return "lottery-updatemega";
			}
		}
		return "redirect:/home";

	}

	@PostMapping(value = "/updateMega")
	public String updateMegaLottery(@ModelAttribute(value = "lottery") Lottery lottery,
			@RequestParam(value = "check-mega1") String check1, @RequestParam(value = "check-mega2") String check2,
			@RequestParam(value = "check-mega3") String check3, @RequestParam(value = "check-mega4") String check4,
			@RequestParam(value = "check-mega5") String check5, @RequestParam(value = "check-mega6") String check6,
			Model model) {
		String[] numberArr = { check1, check2, check3, check4, check5, check6 };
		String winning = String.join(", ", check1, check2, check3, check4, check5, check6);
		if (lotteryService.validateMega(numberArr)) {
			lottery.setWinNumber(winning);
			lotteryService.saveLottery(lottery);
		} else {
			String error = "";
			if (!lotteryService.validateMega(numberArr)) {
				error = "Invalid input: Each number must be different and must be from 1-45!";
			}
			model.addAttribute("error", error);
			lottery.setWinNumber(winning);
			model.addAttribute("lottery", lottery);
			return "lottery-updatemega";
		}
		return "redirect:/lotterymanage";

	}

	@PostMapping(value = "/updateMax")
	public String updateMaxLottery(@ModelAttribute(value = "lottery") Lottery lottery,
			@RequestParam(value = "special-price1") String special1,
			@RequestParam(value = "special-price2") String special2,
			@RequestParam(value = "first-price1") String first1, @RequestParam(value = "first-price2") String first2,
			@RequestParam(value = "first-price3") String first3, @RequestParam(value = "first-price4") String first4,
			@RequestParam(value = "second-price1") String second1,
			@RequestParam(value = "second-price2") String second2,
			@RequestParam(value = "second-price3") String second3,
			@RequestParam(value = "second-price4") String second4,
			@RequestParam(value = "second-price5") String second5,
			@RequestParam(value = "second-price6") String second6, @RequestParam(value = "third-price1") String third1,
			@RequestParam(value = "third-price2") String third2, @RequestParam(value = "third-price3") String third3,
			@RequestParam(value = "third-price4") String third4, @RequestParam(value = "third-price5") String third5,
			@RequestParam(value = "third-price6") String third6, @RequestParam(value = "third-price7") String third7,
			@RequestParam(value = "third-price8") String third8, RedirectAttributes redirectAttributes, Model model) {

		String special = String.join(", ", special1, special2);
		String first = String.join(", ", first1, first2, first3, first4);
		String second = String.join(", ", second1, second2, second3, second4, second5, second6);
		String third = String.join(", ", third1, third2, third3, third4, third5, third6, third7, third8);
		String[] specialArr = special.split(", ");
		String[] firstArr = first.split(", ");
		String[] secondArr = second.split(", ");
		String[] thirdArr = third.split(", ");
		if (lotteryService.validateMax(thirdArr) && lotteryService.validateMax(specialArr)
				&& lotteryService.validateMax(firstArr) && lotteryService.validateMax(secondArr)) {
			lottery.setFirstPrice(first);
			lottery.setSpecialPrice(special);
			lottery.setSecondPrice(second);
			lottery.setThirdPrice(third);
			lotteryService.saveLottery(lottery);
		} else {
			String error = "Each price must have different numbers!";
			lottery.setFirstPrice(first);
			lottery.setSecondPrice(second);
			lottery.setSpecialPrice(special);
			lottery.setThirdPrice(third);
			model.addAttribute("error", error);
			model.addAttribute("lottery", lottery);
			return "lottery-updatemax";
		}
		return "redirect:/lotterymanage";
	}

	@GetMapping(value = "/lotterymanage/{pageNo}")
	public String lotteryPagination(@PathVariable(value = "pageNo") int pageNo, Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				int pageSize = 10;
				Page<Lottery> page = lotteryService.findPaginated(pageNo, pageSize);
				List<Lottery> list = page.getContent();
				model.addAttribute("currentPage", pageNo);
				model.addAttribute("totalPages", page.getTotalPages());
				model.addAttribute("totalItems", page.getTotalElements());
				model.addAttribute("listLottery", list);
				return "lottery-management";
			}
		}
		return "redirect:/home";

	}

	@PostMapping(value = "/checkmax")
	public String checkMax(@RequestParam(value = "check") String checkNumber,
			@RequestParam(value = "date") Date drawDate, RedirectAttributes redirectAttributes, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			Lottery lottery = lotteryService.findByDate(drawDate, "max");
			CheckHistory checkHistory = new CheckHistory();
			java.util.Date currentDate = new java.util.Date();
			String result = lotteryService.checkMax(checkNumber, lottery).equals("Good luck next time!")
					? lotteryService.checkMax(checkNumber, lottery)
					: "Congratulation you have won " + lotteryService.checkMax(checkNumber, lottery);
			String historyResult = lotteryService.checkMax(checkNumber, lottery).equals("Good luck next time!")
					? "No prizes"
					: lotteryService.checkMax(checkNumber, lottery);
			checkHistory.setCheckDate(new java.sql.Date(currentDate.getTime()));
			checkHistory.setCheckNumber(checkNumber);
			checkHistory.setLotteryType("max");
			checkHistory.setDrawDate(drawDate);
			checkHistory.setOutcome(historyResult);
			checkHistory.setLotteryId(lottery.getId());
			checkHistory.setUserId(account.getId());
			checkHistoryService.saveHistory(checkHistory);
			redirectAttributes.addFlashAttribute("result", result);
		} else {
			String error = "Please login first!";
			redirectAttributes.addFlashAttribute("error", error);
		}
		return "redirect:/checkresult";
	}

	@GetMapping(value = "/deletelottery/{id}")
	public String deleteLottery(@PathVariable(value = "id") long id, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				lotteryService.deleteLottery(id);
				return "redirect:/lotterymanage";
			}
		}
		return "redirect:/home";
	}

	@PostMapping(value = "/checkmega")
	public String checkMega(@RequestParam(value = "check-mega1") String check1,
			@RequestParam(value = "check-mega2") String check2, @RequestParam(value = "check-mega3") String check3,
			@RequestParam(value = "check-mega4") String check4, @RequestParam(value = "check-mega5") String check5,
			@RequestParam(value = "check-mega6") String check6, @RequestParam(value = "date") Date date,
			RedirectAttributes redirectAttributes, HttpSession session) {
		String[] arr = { check1, check2, check3, check4, check5, check6 };
		String checkNumberString = check1 + ", " + check2 + ", " + check3 + ", " + check4 + ", " + check5 + ", "
				+ check6;
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (lotteryService.validateMega(arr)) {
				Lottery lottery = lotteryService.findByDate(date, "mega");
				CheckHistory checkHistory = new CheckHistory();
				String result = lotteryService.checkMega(arr, lottery).equals("Good luck next time!")
						? lotteryService.checkMega(arr, lottery)
						: "Congratulation you have won " + lotteryService.checkMega(arr, lottery);
				String historyResult = lotteryService.checkMega(arr, lottery).equals("Good luck next time!")
						? "No prizes"
						: lotteryService.checkMega(arr, lottery);
				java.util.Date currentDate = new java.util.Date();
				checkHistory.setCheckDate(new java.sql.Date(currentDate.getTime()));
				checkHistory.setCheckNumber(checkNumberString);
				checkHistory.setLotteryType("mega");
				checkHistory.setDrawDate(date);
				checkHistory.setOutcome(historyResult);
				checkHistory.setLotteryId(lottery.getId());
				checkHistory.setUserId(account.getId());
				checkHistoryService.saveHistory(checkHistory);
				redirectAttributes.addFlashAttribute("result", result);
			} else {
				String error = "Invalid input: Each number must be different and must be from 1-45!";
				redirectAttributes.addFlashAttribute("error", error);
			}
		} else {
			String error = "Please login first!";
			redirectAttributes.addFlashAttribute("error", error);
		}
		return "redirect:/checkresultmega";
	}

	@PostMapping(value = "/deletemultilottery")
	public String deleteMulLottery(HttpServletRequest request) {
		for (String i : request.getParameterValues("id")) {
			long id = Long.parseLong(i);
			lotteryService.deleteLottery(id);
		}
		return "redirect:/lotterymanage";
	}

	@PostMapping(value = "/searchdate")
	public String searchDate(@RequestParam(value = "search") Date search, Model model) {
		List<Lottery> lottery = lotteryService.getAllLottery();
		List<Lottery> dateList = new ArrayList<Lottery>();
		for (Lottery a : lottery) {
			if (a.getDrawDate().equals(search)) {
				dateList.add(a);
			}
		}
		model.addAttribute("listLottery", dateList);
		return "lottery-management";
	}

}
