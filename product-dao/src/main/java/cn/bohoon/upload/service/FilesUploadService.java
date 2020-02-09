package cn.bohoon.upload.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 * @author HJ
 * 2017年11月10日,上午10:54:52
 */
@Service
public class FilesUploadService {

	@Value("${image.upload.url}")
	String imageUploadUrl;

	@Value("${image.upload.path}")
	String imageUploadPath;

	@Value("${excel.upload.path}")
	String excelUploadPath;
	
	public String handleFileUploadNoDomain(MultipartFile file, String type) throws Exception {
		// 判断文件名
		String fileName = file.getOriginalFilename();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		String uuid = UUID.randomUUID().toString();
		String path = String.format("/upload/%s/%s.%s", type, uuid, fileExt);
		write(Paths.get(imageUploadPath, path), file.getBytes());
		return path;
	}
	
	public void write(Path path, byte[] content) throws IOException {
		if (!Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		Files.write(path, content);
	}

}
