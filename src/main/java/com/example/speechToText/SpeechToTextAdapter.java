package com.example.speechToText;

public class SpeechToTextAdapter {
    private SpeechToText speechToText;

    public SpeechToTextAdapter(SpeechToText speechToText) {
        this.speechToText = speechToText;
    }

    public String getConvertedText() {
        return speechToText.getRecognizedText();
    }
}