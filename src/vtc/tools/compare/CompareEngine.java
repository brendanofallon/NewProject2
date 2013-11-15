package vtc.tools.compare;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.broadinstitute.variant.variantcontext.Allele;
import org.broadinstitute.variant.variantcontext.VariantContext;

import vtc.datastructures.VariantPool;
import vtc.tools.setoperator.IntersectType;
import vtc.tools.setoperator.Operator;
import vtc.tools.setoperator.SetOperator;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class CompareEngine {

	private ArgumentParser parser = null;
	List<File> filesToCompare = new ArrayList<File>();
	
	public CompareEngine(String[] toolArgs) {
		//We currently accept no args besides files
		initialize();
		try {
			Namespace args = parser.parseArgs(toolArgs);
			List<String> vcfs = args.getList("VCF");
			for(String vcf : vcfs) {
				File vFile = new File(vcf);
				filesToCompare.add(vFile);
			}
		} catch (ArgumentParserException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	
	public void doComparison() {
		//Make a list of variant pools out of the list of files..
		VariantPool poolA = new VariantPool(filesToCompare.get(0), filesToCompare.get(0).getAbsolutePath());
		VariantPool poolB = new VariantPool(filesToCompare.get(1), filesToCompare.get(1).getAbsolutePath());
		
		VariantPool perfectMatches = new VariantPool();
		VariantPool mismatchedAlleles = new VariantPool();
		VariantPool uniqA = new VariantPool();
		VariantPool uniqB = new VariantPool(poolB);
		
		Iterator<String> it = poolA.getIterator();
		while(it.hasNext()) {
			String posKey = it.next();
			
			VariantContext ctxtA = poolA.getVariant(posKey);
			VariantContext ctxtB = poolB.getVariant(posKey);
			//There is a variant at this position in both pools
			if (ctxtA != null && ctxtB != null) {
				boolean altsMatch = false;
				boolean refsMatch = false;
				for(Allele aAllele : ctxtA.getAlternateAlleles()) {
					if (ctxtB.getAlternateAlleles().contains(aAllele)) {
						altsMatch = true;
					}
				}

				if (ctxtA.getReference().equals(ctxtB.getReference())) {
					refsMatch = true;
				}

				if (altsMatch && refsMatch) {
					perfectMatches.addVariant(ctxtA);
				}
				else {
					mismatchedAlleles.addVariant(ctxtA);
				}
				
				uniqB.removeVariant(ctxtA);
			}
			
			if (ctxtA != null && ctxtB == null) {
				uniqA.addVariant(ctxtA);
			}
			
		}
		
		System.out.println(" Perfect matches : " + perfectMatches.getCount());
		System.out.println("Unique to " + filesToCompare.get(0) + " : " + uniqA.getCount());
		System.out.println("Unique to " + filesToCompare.get(1) + " : " + uniqB.getCount());
		
	}
	
	private void initialize() {
		parser = ArgumentParsers.newArgumentParser("VarStats");
		parser.description("Variant Stats will perform basic statistical analysis.");
		parser.defaultHelp(true); // Add default values to help menu
		ArgumentGroup Stats = parser.addArgumentGroup("statistical arguments");
		ArgumentGroup output = parser.addArgumentGroup("output arguments");

		Stats.addArgument("-i", "--input")
				.nargs("+")
				.dest("VCF")
				.required(true)
				.type(String.class)
				.help("Specify two or more VCF input files. Multiple files may be "
						+ "specified at once.");
		
	}

	

}
