/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.android.oauth.bus;

import com.squareup.otto.Bus;

/**
 * @author Bruno Farache
 */
public class BusUtil {

	public static void post(Exception exception) {
		getInstance().post(new Exception(exception.getMessage()));
	}

	public static void post(Object event) {
		getInstance().post(event);
	}

	public static void register(Object object) {
		getInstance().register(object);
	}

	public static void unregister(Object object) {
		getInstance().unregister(object);
	}

	protected static Bus getInstance() {
		if (_bus == null) {
			_bus = new Bus();
		}

		return _bus;
	}

	private static Bus _bus;

}