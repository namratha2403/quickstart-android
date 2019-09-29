// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.firebase.samples.apps.mlkit.java.cloudtextrecognition;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import org.apache.commons.lang3.StringUtils;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.samples.apps.mlkit.common.FrameMetadata;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.java.VisionProcessorBase;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Processor for the cloud document text detector demo.
 */
public class CloudDocumentTextRecognitionProcessor
        extends VisionProcessorBase<FirebaseVisionDocumentText> {

    private static final String TAG = "CloudDocTextRecProc";

    public static String date = "";
    public static String finalAmount = "";
    public ArrayList<String> Amounts = new ArrayList<>();
    public ArrayList<String> finalList = new ArrayList<>();
    public static boolean dateM = false;

    private final FirebaseVisionDocumentTextRecognizer detector;

    public CloudDocumentTextRecognitionProcessor() {
        super();
        detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer();
    }

    @Override
    protected Task<FirebaseVisionDocumentText> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull FirebaseVisionDocumentText text,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();

        Log.d(TAG, "detected text is: " + text.getText());
        List<FirebaseVisionDocumentText.Block> blocks = text.getBlocks();
        for (int i = 0; i < blocks.size(); i++) {
            ValidateText(blocks.get(i).getText());
            List<FirebaseVisionDocumentText.Paragraph> paragraphs = blocks.get(i).getParagraphs();
            for (int j = 0; j < paragraphs.size(); j++) {
                List<FirebaseVisionDocumentText.Word> words = paragraphs.get(j).getWords();
                for (int l = 0; l < words.size(); l++) {
                    CloudDocumentTextGraphic cloudDocumentTextGraphic =
                            new CloudDocumentTextGraphic(graphicOverlay,
                                    words.get(l));
                    graphicOverlay.add(cloudDocumentTextGraphic);
                }
            }
        }
        for(String i:Amounts){
            if(!StringUtils.equalsAnyIgnoreCase(i,"")){
                finalList.add(i);
            }
        }
        System.out.println( "Amount: " + Amounts);
        finalAmount = finalList.get(finalList.size() -1 );
        System.out.println("date: " + date + "  Amount: " + finalAmount);

        graphicOverlay.postInvalidate();
    }

    public void ValidateText(String s){
        String[] keys = s.split("\\n");
        for (int count =0; count<keys.length;count++){
            Pattern datePattern  = Pattern.compile(".*(date|Date|DATE).*");
            Matcher dateMatch = datePattern.matcher(keys[count]);
            if(dateMatch.find()){
                dateM = true;
            }
            Pattern pattern1 = Pattern.compile("\\d{1,}(\\/|\\-)\\d{1,}(\\/|\\-)?\\d{0,}");
            Matcher matcher1 = pattern1.matcher(keys[count]);
            if ((matcher1.find()) && dateM )
            {
                System.out.println(matcher1.group(0));
                date = matcher1.group(0);
                dateM = false;
            }else {
                if(StringUtils.getDigits(keys[count]) != null && !StringUtils.getDigits(keys[count]).startsWith("0")){
                    String Amount = StringUtils.getDigits(keys[count]);
                    Amounts.add(Amount);
                }
            }
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Cloud Document Text detection failed." + e);
    }
}
