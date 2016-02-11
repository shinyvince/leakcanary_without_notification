/*
 * Copyright (C) 2015 Square, Inc.
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

package com.squareup.leakcanary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static com.squareup.leakcanary.LeakCanary.leakInfo;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.findNextAvailableHprofFile;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.leakResultFile;

/**
 * You can extend this class and override
 * {@link #afterDefaultHandling(HeapDump, AnalysisResult, String)} to add custom
 * behavior, e.g. uploading the heap dump.
 */
public class DisplayLeakService extends AbstractAnalysisResultService {

    private static final int MAX_STORED_LEAKS = 20;

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @TargetApi(HONEYCOMB)
    @Override
    protected final void onHeapAnalyzed(HeapDump heapDump, AnalysisResult result) {
        String leakInfo = leakInfo(this, heapDump, result);
        Log.e("LeakCanary", leakInfo);

        if (!result.leakFound || result.excludedLeak) {
            afterDefaultHandling(heapDump, result, leakInfo);
            return;
        }

        int maxStoredLeaks = MAX_STORED_LEAKS;
        File renamedFile = findNextAvailableHprofFile(maxStoredLeaks);

        if (renamedFile == null) {
            // No file available.
            Log.e("LeakCanary",
                    "Leak result dropped because we already store " + maxStoredLeaks
                            + " leak traces.");
            afterDefaultHandling(heapDump, result, leakInfo);
            return;
        }

        heapDump = heapDump.renameFile(renamedFile);

        File resultFile = leakResultFile(renamedFile);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(resultFile));
            oos.writeObject(heapDump);
            oos.writeObject(result);
        } catch (IOException e) {
            Log.e("LeakCanary", "Could not save leak analysis result to disk", e);
            afterDefaultHandling(heapDump, result, leakInfo);
            return;
        } finally {
            IOUtils.closeSilently(oos);
        }

        afterDefaultHandling(heapDump, result, leakInfo);
    }

    /**
     * You can override this method and do a blocking call to a server to upload
     * the leak trace and the heap dump. Don't forget to check
     * {@link AnalysisResult#leakFound} and {@link AnalysisResult#excludedLeak}
     * first.
     */
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
    }
}
