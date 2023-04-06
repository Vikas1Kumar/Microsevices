package question.bank.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import question.bank.service.ExtractDataFromWordFile;

@Controller
public class MainController {

	@RequestMapping("/upload-word-file")
	public String uploadQuestion() {
		System.out.println("File form handler");
		return "upload-file";
	}

	@RequestMapping(value = "upload-file", method = RequestMethod.POST)
	public String getQUestion(@RequestParam("formFile") MultipartFile file, Model model, HttpSession session) {
		try {
			// File fileNew = new File(file.getOriginalFilename());
			String path = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources"
					+ File.separator + "doc" + File.separator + file.getOriginalFilename();
			String pathXML = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources"
					+ File.separator + "OMML2MML.XSL";
			byte[] data = file.getBytes();
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(data);
			fos.close();
			System.out.println("Path: " + path);
			ExtractDataFromWordFile edfwf = new ExtractDataFromWordFile();
			ArrayList<ArrayList<ArrayList<String>>> tables = edfwf.getDataFromWord(path, pathXML);
			System.out.println("File upload handler");
			System.out.println("File size: " + file.getSize());
			System.out.println("File getContentType: " + file.getContentType());
			System.out.println("File getName: " + file.getName());
			System.out.println("File getOriginalFilename: " + file.getOriginalFilename());
			System.out.println("File getOriginalFilename: " + file.getResource());
					//.getStorageDescription());
			System.out.println();
			model.addAttribute("tables", tables);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//return "uploaded-text";
		return "uploaded-file-json";
	}
}
