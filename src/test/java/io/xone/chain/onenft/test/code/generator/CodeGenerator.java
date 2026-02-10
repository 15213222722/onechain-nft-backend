package io.xone.chain.onenft.test.code.generator;

import java.util.Collections;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class CodeGenerator {

	public static void main(String[] args) {
		// 1. Database Configuration
		String url = "jdbc:mysql://localhost:3306/nft_new?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
		String username = "root";
		String password = "abcd.1234";

		// 2. Project Path (Get the root directory of the project)
		String projectPath = System.getProperty("user.dir");

		FastAutoGenerator.create(url, username, password).globalConfig(builder -> {
			builder.author("GitHub Copilot") // Set author
					.enableSwagger() // Enable Swagger annotations
					.disableOpenDir().outputDir(projectPath + "/src/main/java"); // Output directory
		}).packageConfig(builder -> {
			builder.parent("io.xone.chain.onenft") // Set parent package
//					.moduleName("system") // Set module name (optional)
					.pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")); // Set
																														// Mapper
																														// XML
																														// path
		}).strategyConfig(builder -> {
			builder.addInclude("user_activities"); // Set tables to generate (comma-separated needed)

			// Entity Strategy
			builder.entityBuilder().enableLombok() // Enable Lombok
					.enableFileOverride().enableTableFieldAnnotation(); // Enable field annotations

			// Controller Strategy
			builder.controllerBuilder().enableRestStyle(); // Enable RestController
		}).templateEngine(new FreemarkerTemplateEngine()) // Use Freemarker engine
				.execute();
	}
}
