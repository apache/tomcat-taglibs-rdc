/*
 *    
 *   Copyright 2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
/*$Id$*/
package org.apache.taglibs.rdc;

import java.lang.IllegalArgumentException;
import java.lang.NoSuchMethodException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Utility methods for the rdc package
 * 
 * @author Rahul Akolkar
 */
public class RDCUtils {

	// ******************
	// PRIVATE CONSTANTS
	// ******************
	private static final String RDC_PREFIX = "__rdc";
	private static int varCounter = 71463;

	// ******************
	// PUBLIC METHODS
	// ******************			
	/** 
	 * Return true if the given class implements the given interface.
	 *
	 * @param Class clas
	 * @param Class interfayce
	 */
	public static boolean implementsInterface(Class clas, Class interfayce) {
		while (clas != null && clas != Object.class) {
			Class[] implementedInterfaces = clas.getInterfaces();
			for (int j = 0; j < implementedInterfaces.length; j++) {
				if (implementedInterfaces[j] == interfayce) {
					// nemo !
					return true;
				}
			}
			clas = clas.getSuperclass();
		}
		return false;
	}
	
	/** 
	 * Return true if the given class defines or inherits the given field.
	 *
	 * @param Class clas
	 * @param String field
	 */
	public static boolean hasField(Class clas, String field) {
		while (clas != null && clas != Object.class) {
			Field[] fields = clas.getDeclaredFields();
			for (int j = 0; j < fields.length; j++) {
				if (((Field)fields[j]).getName() == field) {
					return true;
				}
			}
			clas = clas.getSuperclass();
		}
		return false;
	}
	
	
	/** 
	 * Return true if the given class defines or inherits the given method.
	 *
	 * @param Class clas
	 * @param String method
	 */
	public static boolean hasMethod(Class clas, String methodName,
		Class[] paramTypes) {
		while (clas != null && clas != Object.class) {
			Method method = null;
			try {
				method = clas.getDeclaredMethod(methodName, paramTypes);
			} catch (NoSuchMethodException nsme) {
				clas = clas.getSuperclass();
				continue;
			}
			return true;
		}
		return false;
	}
	
	/** 
	 * Return the Class for this class name, if such a class exists, 
	 * else return null.
	 *
	 * @param String className
	 */
	public static Class getClass(String className) {
		try {
			Class clas = Class.forName(className);
			return clas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 
	 * Throw an IllegalArgumentException if the supplied string is null or
	 * empty.
	 *
	 * @param String str
	 * @param String err_msg
	 */
	public static void mustExist(String str, String err_msg) {
		if (str == null || str.trim().length() == 0) {
			throw new IllegalArgumentException(err_msg);
		}
	}
	
	/** 
	 * Throw an IllegalArgumentException if the supplied condition is not
	 * satisfied.
	 *
	 * @param boolean cond
	 * @param String err_msg
	 */
	public static void mustSatisfy(boolean cond, String err_msg) {
		if (!cond) {
			throw new IllegalArgumentException(err_msg);
		}
	}
	
	/** 
	 * Print warning via IllegalArgumentException if the supplied error
	 * condition holds, but move on.
	 *
	 * @param boolean cond
	 * @param String err_msg
	 */
	public static void warnIf(boolean cond, String err_msg) {
		if (cond) {
			IllegalArgumentException iae = new IllegalArgumentException(err_msg);
			iae.printStackTrace();
		}
	}
		
	/** 
	 * Return true if this string contains non-white space characters.
	 *
	 * @param String str
	 */
	public static boolean isStringEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/** 
	 * Create a temporary variable name for this groupTag's page context 
	 *
	 */
	public static String getNextRDCVarName() {
		if (varCounter == Integer.MAX_VALUE) {
			varCounter = 0;
		}
		varCounter++;
		return RDC_PREFIX + varCounter; 
	}

}
