package cn.bohoon.basicsetup.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.domain.TemplatePrintType;
import cn.bohoon.basicSetup.entity.TemplatePrint;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.basicSetup.service.TemplatePrintService;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "printOrder")
public class OrderPrintSetupController {

	@Autowired
	private TemplatePrintService templatePrintService;

	@Autowired
	SysParamService sysParamService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	//printOrder/setup
	@RequestMapping(value = "setup")
	public String show() {
		//System.out.println("打印模板");
		return "basicSetup/printOrder/printOrderSetup";
	}

	@RequestMapping(value = "shoppingList", method = RequestMethod.GET)
	public String getShoppingList(Model model) {
		
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_SHOPPING_LIST);
		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);
		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/printShoppingList";
	}
    
	
	@RequestMapping(value = "packingList", method = RequestMethod.GET)
	public String getPackingList(Model model) {
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_PACKING_LIST);
		if (templatePrint == null) {
			templatePrint = new TemplatePrint();
			templatePrint.setTemplatePrintType(TemplatePrintType.PRINT_PACKING_LIST);
			templatePrint.setTemplateChoose(2);
		}

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/printPackingList";
	}

	@RequestMapping(value = "shoppinngAndPacking", method = RequestMethod.GET)
	public String getShoppinngAndPacking(Model model) {
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_SHOPPING_AND_PACKING);
		if (templatePrint == null) {
			templatePrint = new TemplatePrint();
			templatePrint.setTemplatePrintType(TemplatePrintType.PRINT_SHOPPING_AND_PACKING);
			templatePrint.setTemplateChoose(2);
		}

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/printShoppinngAndPacking";
	}

	@RequestMapping(value = "exchangeList", method = RequestMethod.GET)
	public String getExchangeList(Model model) {
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_EXCHANGE_LIST);
		if (templatePrint == null) {
			templatePrint = new TemplatePrint();
			templatePrint.setTemplatePrintType(TemplatePrintType.PRINT_EXCHANGE_LIST);
			templatePrint.setTemplateChoose(1);
		}

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/printPrintExchangelist";
	}

	@RequestMapping(value = "exchangePackingList", method = RequestMethod.GET)
	public String getExchangePackingList(Model model) {
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_EXCHANGE_PACKING_LIST);
		if (templatePrint == null) {
			templatePrint = new TemplatePrint();
			templatePrint.setTemplatePrintType(TemplatePrintType.PRINT_EXCHANGE_PACKING_LIST);
			templatePrint.setTemplateChoose(2);
		}

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/printExchangePackingList";
	}

	@RequestMapping(value = "exchangeAndPacking", method = RequestMethod.GET)
	public String getExchangeAndPacking(Model model) {
	
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_EXCHANGE_AND_PACKING);
		if (templatePrint == null) {
			templatePrint = new TemplatePrint();
			templatePrint.setTemplatePrintType(TemplatePrintType.PRINT_EXCHANGE_AND_PACKING);
			templatePrint.setTemplateChoose(2);
		}

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/printExchangePackingList";
	}

	@ResponseBody
	@RequestMapping(value = "OrderTemplatesModify")
	public String OrderTemplatesModify(HttpServletRequest request, TemplatePrintType templatePrintType,
			Integer templateChoose, @RequestParam(name = "qRcode", defaultValue = "false") Boolean qRcode,
			@RequestParam(name = "orderId", defaultValue = "false") Boolean orderId,
			@RequestParam(name = "pimages", defaultValue = "false") Boolean pimages,
			@RequestParam(name = "barcodes", defaultValue = "false") Boolean barcodes) {
	
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				templatePrintType);
		if (templatePrint == null) {            
			templatePrint = new TemplatePrint();
			templatePrint.setTemplatePrintType(templatePrintType);
		}
		templatePrint.setTemplateChoose(templateChoose);
		templatePrint.setqRcodeState(qRcode);
		templatePrint.setOrderIdState(orderId);
		if (templateChoose == 2) {
			templatePrint.setPrintImageState(pimages);
			templatePrint.setBarcodesState(barcodes);
		}
		templatePrintService.save(templatePrint);
		return ResultUtil.getSuccessMsg();
	}
	
	@RequestMapping(value="CollectionList", method = RequestMethod.GET)
	public String  toCollectproduct(Model model) {
		System.out.println("哈哈哈");
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_COLLECTION_LIST);
		if(null==templatePrint){
			 templatePrint=new TemplatePrint();
			 templatePrint.setTemplatePrintType(TemplatePrintType.PRINT_COLLECTION_LIST);
		}
		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);
		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/CollectionList";
	}
	
	@RequestMapping(value="CollectionEdict", method = RequestMethod.GET)
	public String  EdictCollectproduct(Model model) {
		
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PRINT_COLLECTION_LIST);
		if(null==templatePrint){
			 templatePrint=new TemplatePrint();
			 templatePrint.setTemplatePrintType(TemplatePrintType.PRINT_COLLECTION_LIST);
		}
		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);
		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/CollectionList";
		
		
	}
	@RequestMapping(value="CollectionPacking", method = RequestMethod.GET)
	public String  CollectionPacking(Model model) {
		
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.PICKING_COLLECTION_LIST);
		if(null==templatePrint){
			 templatePrint=new TemplatePrint();
			 templatePrint.setTemplatePrintType(TemplatePrintType.PICKING_COLLECTION_LIST);
			 templatePrint.setTemplateChoose(2);
		}
		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);
		model.addAttribute("templatePrint", templatePrint);
		System.out.println(templatePrint.getBarcodesState());
		return "basicSetup/printOrder/CollectionPacking";
	}
	
	

	@RequestMapping(value = "collectPackingUnion", method = RequestMethod.GET)
	public String collectPackingUnion(Model model) {
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				TemplatePrintType.UNLION_COLLECT_LIST);
		if (templatePrint == null) {
			templatePrint = new TemplatePrint();
			templatePrint.setTemplatePrintType(TemplatePrintType.UNLION_COLLECT_LIST);
			templatePrint.setTemplateChoose(2);
		}

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		model.addAttribute("templatePrint", templatePrint);
		return "basicSetup/printOrder/collectPackingUnion";
	}
   
	
	//所以页面的返回功能
	@ResponseBody
	@RequestMapping(value = "allfan")
	public String OrderTemplatesModify(HttpServletRequest request) {
	
		return ResultUtil.getSuccessMsg();
	}
	
}
