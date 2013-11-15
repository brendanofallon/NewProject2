package vtc.tools.compare;

import java.util.Iterator;

import org.broadinstitute.variant.variantcontext.Allele;
import org.broadinstitute.variant.variantcontext.VariantContext;

import vtc.datastructures.VariantPool;

/**
 * Some static methods (functions, I guess they call them) that compute things about variant pools
 * and variant contexts
 * @author brendanofallon
 *
 */
public class VariantPoolCalculator {

	public static int countSNPs(VariantPool vp) {
		Iterator<String> it = vp.getIterator();
		int snps = 0;
		while(it.hasNext()) {
			String key = it.next();
			VariantContext v = vp.getVariant(key);
			if ( v.isSNP() ) {
				snps++;
			}
		}
		return snps;
	}
	
	public static int countIndels(VariantPool vp) {
		Iterator<String> it = vp.getIterator();
		int indels = 0;
		while(it.hasNext()) {
			String key = it.next();
			VariantContext v = vp.getVariant(key);
			if ( v.isIndel() ) {
				indels++;
			}
		}
		return indels;
	}
	
	public static int countMNPs(VariantPool vp) {
		Iterator<String> it = vp.getIterator();
		int mnps = 0;
		while(it.hasNext()) {
			String key = it.next();
			VariantContext v = vp.getVariant(key);
			if ( v.isMNP() ) {
				mnps++;
			}
		}
		return mnps;
	}
	
	public static boolean isTransition(VariantContext v) {
		if (v.isSNP()) {
			Allele ref = v.getReference();
			Allele alt = v.getAltAlleleWithHighestAlleleCount();
			String refBase = ref.getBaseString();
			String altbase = alt.getBaseString();
			if ( (refBase.equals("A") && altbase.equals("G"))
					|| (refBase.equals("G") && altbase.equals("A"))
					|| (refBase.equals("C") && altbase.equals("T"))
					|| (refBase.equals("T") && altbase.equals("C"))) {
				return true;	
			}
		}
							
		return false;		
	}
	
	public static boolean isTransversion(VariantContext v) {
		return v.isSNP() && (! isTransition(v));
	}
	
	public static double tiTv(VariantPool vp) {
		double tis = 0.0;
		double tvs = 0.0;
		Iterator<String> it = vp.getIterator();
		while(it.hasNext()) {
			String key = it.next();
			VariantContext v = vp.getVariant(key);
			if (v.isSNP()) {
				if (isTransition(v)) {
					tis++;
				}
				else {
					tvs++;
				}
			}
		}
		
		return tis / tvs;
	}
	
}
