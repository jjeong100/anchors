package com.anchors.database;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;

//import net.sourceforge.pmd.PMDConfiguration;
//import net.sourceforge.pmd.PmdAnalysis;
//import net.sourceforge.pmd.RulePriority;
//import net.sourceforge.pmd.lang.LanguageRegistry;
//import net.sourceforge.pmd.renderers.Renderer;
//import net.sourceforge.pmd.renderers.XMLRenderer;

public class PMD_main {

	public static void main(String[] args) throws IOException {
////		category/apex/bestpractices.xml/ApexUnitTestClassShouldHaveAsserts
////		ApexXSSFromURLParam;
////		ApexUnitTestShouldNotUseSeeAllDataTrue;
//        PMDConfiguration configuration = new PMDConfiguration();
//        configuration.setMinimumPriority(RulePriority.MEDIUM);
//        configuration.addRuleSet("rulesets/java/quickstart.xml");
//
////        configuration.setInputPaths("/home/workspace/src/main/java/code");
//        configuration.setInputPaths("D:\\project\\anchors\\src\\main\\java");
//
//        configuration.setDefaultLanguageVersion(LanguageRegistry.findLanguageByTerseName("java").getVersion("17"));
////        configuration.prependAuxClasspath("/home/workspace/target/classes");
//        configuration.prependAuxClasspath("D:\\project\\anchors\\target\\classes");
//        
//
//        configuration.setReportFormat("xml");
//        configuration.setReportFile("D:\\project\\anchors\\pmd-report.xml");
//
//        Writer rendererOutput = new StringWriter();
//        Renderer renderer = createRenderer(rendererOutput);
//
//        try (PmdAnalysis pmd = PmdAnalysis.create(configuration)) {
//            // optional: add more rulesets
////            pmd.addRuleSet(pmd.newRuleSetLoader().loadFromResource("custom-ruleset.xml"));
////            pmd.addRuleSet(pmd.newRuleSetLoader().loadFromResource("D:\\project\\anchors\\Connex_PMD_Rule.xml"));
//            pmd.addRuleSet(pmd.newRuleSetLoader().loadFromResource("D:\\project\\anchors\\Sample_Rule.xml"));
//            // optional: add more files
//            pmd.files().addFile(Paths.get("src", "main", "more-java", "ExtraSource.java"));
//            // optional: add more renderers
//            pmd.addRenderer(renderer);
//
//            // or just call PMD
//            pmd.performAnalysis();
//        }
//
//        System.out.println("Rendered Report:");
//        System.out.println(rendererOutput.toString());
    }

//    private static Renderer createRenderer(Writer writer) {
//        XMLRenderer xml = new XMLRenderer("UTF-8");
//        xml.setWriter(writer);
//        return xml;
//    }
	
//	 public static void main(String[] args) {
//	        PMDConfiguration configuration = new PMDConfiguration();
////	        configuration.setInputPaths("/home/workspace/src/main/java/code");
//	        configuration.setInputPaths("D:\\project\\anchors\\src\\main\\java");
////	        configuration.addRuleSet("rulesets/java/quickstart.xml");
//	        configuration.addRuleSet("D:\\project\\anchors\\Connex_PMD_Rule.xml");
//	        
////	        pmd.addRuleSet(pmd.newRuleSetLoader().loadFromResource("Connex_PMD_Rule.xml"));
//	        configuration.setReportFormat("xml");
////	        configuration.setReportFile("/home/workspace/pmd-report.xml");
//	        configuration.setReportFile("D:\\project\\anchors\\pmd-report.xml");
//
//	         PMD.runPmd(configuration);
//	    }
}
