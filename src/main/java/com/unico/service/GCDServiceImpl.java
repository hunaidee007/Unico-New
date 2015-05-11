package com.unico.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 
 * @author hunaid.husain
 *
 */
@Service
public class GCDServiceImpl implements GCDService{

	
	public Integer calculateGCD(int num1, int num2) {
		List<Integer> firstSetOfDivisors = findDivisiors(num1);
		List<Integer> secondSetOfDivisors = findDivisiors(num2);
		
		List<Integer>commonDivisors=new ArrayList<Integer>(firstSetOfDivisors);
		commonDivisors.retainAll(secondSetOfDivisors); // only retains the common attributes.

		Integer gcd =Collections.max(commonDivisors); // find the maximum attribute
		return gcd;
	}
	private static List<Integer> findDivisiors(int num) {
		List<Integer> s = new ArrayList<Integer>();
		for (int i = num; i >= 1; i--) {
			// System.out.println(i);
			if (num % i == 0) {
			//	System.out.println(i);
				s.add(i);
			}

		}
		return s;
	}

}
