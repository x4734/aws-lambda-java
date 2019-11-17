package demo.lambda.fuction.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.S3Object;

public class ReadS3File implements RequestHandler<S3Event, String> {
	private final String TXT_TYPE = "txt";
	private final String ACCESS_KEY_ID="AAKIAT6IBL7EKWJHADHVRA";
	private final String SECRET_ACCESS_KEY="Ag5CWcAeqqvQej/0neq8C2R/kBtIi0iNGxMPbFzHvA";
	public String handleRequest(S3Event s3event, Context context) {
		StringBuffer sf = new StringBuffer();
		try {
			context.getLogger().log("started ............ ");
			S3EventNotificationRecord record = s3event.getRecords().get(0);

			String srcBucket = record.getS3().getBucket().getName();
			context.getLogger().log("srcBucket ............ "+srcBucket);
			// Object key may have spaces or unicode non-ASCII characters.
			String srcKey = record.getS3().getObject().getUrlDecodedKey();
			context.getLogger().log("srcKey ............ "+srcKey);
			// Infer the image type.
			Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(srcKey);
			if (!matcher.matches()) {
				System.out.println("Unable to infer image type for key " + srcKey);
				context.getLogger().log("Unable to infer image type for key " + srcKey);
				return "";
			}
			String textType = matcher.group(1);
			if (!(TXT_TYPE.equals(textType))) {
				System.out.println(" Not the text type the file::   " + srcKey + " upload the valid txt type in bucket");
				context.getLogger().log(" Not the text type the file::   " + srcKey + " upload the valid txt type in bucket");
				return "";
			}
			// Download the image from S3 into a stream
		//	AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
			AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
			AmazonS3 s3client = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
			try (final S3Object s3Object = s3client.getObject(srcBucket, srcKey);
			     final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(),
							StandardCharsets.UTF_8);
					final BufferedReader reader = new BufferedReader(streamReader)) {
				String value;
				System.out.println("\n\n******************************* FILE READING START *****************");
				System.out.println("\t\t\t\t\t                                        "+ srcKey +"                                     ");
				System.out.println("******************************* FILE READING START  *****************");
				while ((value = reader.readLine()) != null) {
					sf.append(value + "\n");
				}
														
				System.out.println("\n\n" + sf);
				System.out.println("\n\n******************************* FILE READING END  *****************");
				System.out.println("\t\t\t\t\t                                      "+ srcKey +"                                     ");
				System.out.println("******************************* FILE READING END  *****************");
			} catch (final IOException e) {
				e.printStackTrace();
			}

		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sf.toString();
	}
}
