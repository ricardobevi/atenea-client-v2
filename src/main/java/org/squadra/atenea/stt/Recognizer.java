package org.squadra.atenea.stt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.squadra.atenea.exceptions.GoogleTTSException;

/**
 * Class that submits FLAC audio and retrieves recognized text
 */
public class Recognizer {

    /**
     * URL to POST audio data and retrieve results
     */
    private static final String GOOGLE_RECOGNIZER_URL_NO_LANG = "https://www.google.com/speech-api/v1/recognize?xjerr=1&client=chromium&lang=";

    /**
     * Constructor
     */
    public Recognizer() {

    }

    /**
     * Get recognized data from a Wave file.  This method will encode the wave file to a FLAC
     *
     * @param waveFile Wave file to recognize
     * @param language Language code.  This language code must match the language of the speech to be recognized. ex. en-US ru-RU
     * @return Returns a GoogleResponse, with the response and confidence score
     * @throws UnsupportedAudioFileException 
     * @throws IOException 
     * @throws GoogleTTSException 
     * @throws Exception Throws exception if something goes wrong
     */
    public GoogleResponse getRecognizedDataForWave(File waveFile, String language) throws IOException, GoogleTTSException {
        FlacEncoder flacEncoder = new FlacEncoder();
        File flacFile = new File(waveFile + ".flac");

        flacEncoder.convertWaveToFlac(waveFile, flacFile);

        String response = rawRequest(flacFile, language);

        //Delete converted FLAC data
        flacFile.delete();

        String[] parsedResponse = parseResponse(response);

        GoogleResponse googleResponse = new GoogleResponse();

        if (parsedResponse != null) {
            googleResponse.setResponse(parsedResponse[0]);
            googleResponse.setConfidence(parsedResponse[1]);
        } else {
            googleResponse.setResponse(null);
            googleResponse.setConfidence(null);
        }

        return googleResponse;
    }

    /**
     * Get recognized data from a Wave file.  This method will encode the wave file to a FLAC
     *
     * @param waveFile Wave file to recognize
     * @param language Language code.  This language code must match the language of the speech to be recognized. ex. en-US ru-RU
     * @return Returns a GoogleResponse, with the response and confidence score
     * @throws GoogleTTSException 
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     * @throws Exception Throws exception if something goes wrong
     */
    public GoogleResponse getRecognizedDataForWave(String waveFile, String language) throws IOException, GoogleTTSException {
        return getRecognizedDataForWave(new File(waveFile), language);
    }

    /**
     * Get recognized data from a FLAC file.
     *
     * @param flacFile FLAC file to recognize
     * @param language Language code.  This language code must match the language of the speech to be recognized. ex. en-US ru-RU
     * @return Returns a GoogleResponse, with the response and confidence score
     * @throws GoogleTTSException 
     * @throws Exception Throws exception if something goes wrong
     */
    public GoogleResponse getRecognizedDataForFlac(File flacFile, String language) throws GoogleTTSException {
        String response = rawRequest(flacFile, language);
        String[] parsedResponse = parseResponse(response);

        GoogleResponse googleResponse = new GoogleResponse();


        if (parsedResponse != null) {
            googleResponse.setResponse(parsedResponse[0]);
            googleResponse.setConfidence(parsedResponse[1]);
        } else {
            googleResponse.setResponse(null);
            googleResponse.setConfidence(null);
        }

        return googleResponse;
    }

    /**
     * Get recognized data from a FLAC file.
     *
     * @param flacFile FLAC file to recognize
     * @param language Language code.  This language code must match the language of the speech to be recognized. ex. en-US ru-RU
     * @return Returns a GoogleResponse, with the response and confidence score
     * @throws GoogleTTSException 
     * @throws Exception Throws exception if something goes wrong
     */
    public GoogleResponse getRecognizedDataForFlac(String flacFile, String language) throws GoogleTTSException {
        return getRecognizedDataForFlac(new File(flacFile), language);
    }

    /**
     * Parses the raw response from Google
     *
     * @param rawResponse The raw, unparsed response from Google
     * @return Returns the parsed response.  Index 0 is response, Index 1 is confidence score
     */
    private String[] parseResponse(String rawResponse) {
        if (!rawResponse.contains("utterance"))
            return null;

        String[] parsedResponse = new String[2];

        String[] strings = rawResponse.split(":");

        parsedResponse[0] = strings[4].split("\"")[1];
        parsedResponse[1] = strings[5].replace("}]}", "");

        return parsedResponse;
    }

    /**
     * Performs the request to Google with a file <br>
     * Request is buffered
     *
     * @param inputFile Input files to recognize
     * @return Returns the raw, unparsed response from Google
     * @throws GoogleTTSException 
     * @throws Exception Throws exception if something went wrong
     */
    private String rawRequest(File inputFile, String language) throws GoogleTTSException {
        URL url;
        URLConnection urlConn;
        OutputStream outputStream;
        BufferedReader br;
        String response = "";

        try {
	        // URL of Remote Script.
	        url = new URL(GOOGLE_RECOGNIZER_URL_NO_LANG + language);
	
	        // Open New URL connection channel.
	        urlConn = url.openConnection();
	
	        // we want to do output.
	        urlConn.setDoOutput(true);
	
	        // No caching
	        urlConn.setUseCaches(false);
	
	        // Specify the header content type.
	        urlConn.setRequestProperty("Content-Type", "audio/x-flac; rate=8000");
	
	        // Send POST output.
	        outputStream = urlConn.getOutputStream();
	
	
	        FileInputStream fileInputStream = new FileInputStream(inputFile);
	
	        byte[] buffer = new byte[256];
	
	        while ((fileInputStream.read(buffer, 0, 256)) != -1) {
	            outputStream.write(buffer, 0, 256);
	        }
	
	        fileInputStream.close();
	        outputStream.close();
	
	        // Get response data.
	        br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
	
	        response = br.readLine();
	
	        br.close();
	
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new GoogleTTSException(e.getMessage());
        }

	    return response;
    }


}
