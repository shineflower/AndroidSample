/*
 * Copyright 2011 woozzu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jackie.sample.utils;

import android.text.TextUtils;

public class StringMatcher {
	/**
	 * @param value    item的文本
	 * @param key      索引列表中的文本
     * @return         查找结果
     */
	public static boolean match(String value, String key) {
		if (TextUtils.isEmpty(value) || TextUtils.isEmpty(key)) {
			return false;
		}

		if (key.length() > value.length()) {
			return false;
		}

//		int i = 0, j = 0;
//		do {
//			if (value.charAt(i) == key.charAt(j)) {
//				i++;
//				j++;
//			} else if (j > 0) {
//				break;
//			} else {
//				i++;
//			}
//		} while (i < value.length() && j < key.length());
//
//		return j == key.length();

		return value.contains(key);
	}
}
