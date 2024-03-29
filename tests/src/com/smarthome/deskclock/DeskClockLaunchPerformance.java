/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smarthome.deskclock;

import android.app.Activity;
import android.test.LaunchPerformanceBase;
import android.os.Bundle;

/**
 * Instrumentation class for DeskClock launch performance testing.
 */
public class DeskClockLaunchPerformance extends LaunchPerformanceBase {

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);

        mIntent.setClassName(getTargetContext(), "com.smarthome.deskclock.DeskClock");
        start();
    }

    /**
     * Calls LaunchApp and finish.
     */
    @Override
    public void onStart() {
        super.onStart();
        LaunchApp();
        finish(Activity.RESULT_OK, mResults);
    }
}
