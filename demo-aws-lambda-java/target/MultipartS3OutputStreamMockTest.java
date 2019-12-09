package om.howtodoinjava.demo.MavenExamplesChild;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;

public class MultipartS3OutputStreamMockTest {

	@InjectMocks
	S3ClientDemo s3ClientDemo = new S3ClientDemo();

	@Mock
	S3Client s3Client;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void s3PutObjectTest() {
		try {
			System.out.println("start s3PutObjectTest class **********" + s3Client);
			PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket("bucket").key("key").serverSideEncryption(ServerSideEncryption.AWS_KMS)
					.ssekmsKeyId("kmsKeyARN").build();
			PutObjectResponse putObjectResponse = PutObjectResponse.builder()
					.serverSideEncryption(ServerSideEncryption.AWS_KMS).ssekmsKeyId("kmsKeyARN").build();
			Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class)))
					.thenReturn(putObjectResponse);
			PutObjectResponse response = s3ClientDemo.s3PutObject("src/test/resources/s3InputFile.txt");
			Mockito.verify(s3Client).putObject(Mockito.eq(putObjectRequest),Mockito.any(RequestBody.class));
	        assertEquals(putObjectResponse.serverSideEncryption(), response.serverSideEncryption());
			System.out.println("end s3PutObjectTest class **********" + s3Client);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	@Test
	public void createMultipartUploadTest() {
		try {
			System.out.println("start createMultipartUploadTest class **********" + s3Client);
			CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
					.bucket("bucket").key("key").serverSideEncryption(ServerSideEncryption.AWS_KMS)
                    .ssekmsKeyId("kmsKeyARN").build();
			CreateMultipartUploadResponse createMultipartUploadResponse = CreateMultipartUploadResponse.builder()
					.bucket("bucket").key("key")
					.serverSideEncryption(ServerSideEncryption.AWS_KMS).ssekmsKeyId("kmsKeyARN").build();
			Mockito.when(s3Client.createMultipartUpload(Mockito.any(CreateMultipartUploadRequest.class)))
					.thenReturn(createMultipartUploadResponse);
			CreateMultipartUploadResponse response = s3ClientDemo.s3CreateMultipartUpload("src/test/resources/s3InputFile.txt");
			Mockito.verify(s3Client).createMultipartUpload(Mockito.eq(createMultipartUploadRequest));
	        assertEquals(createMultipartUploadResponse.serverSideEncryption(), response.serverSideEncryption());
	    	System.out.println("end createMultipartUploadTest class **********" + s3Client);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	@Test
	public void uploadPartTest() {
		try {
			System.out.println("start uploadPartTest class **********" + s3Client);
			CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
					.bucket("bucket").key("key").serverSideEncryption(ServerSideEncryption.AWS_KMS)
                    .ssekmsKeyId("kmsKeyARN").build();
			CreateMultipartUploadResponse createMultipartUploadResponse = CreateMultipartUploadResponse.builder()
					.bucket("bucket").key("key")
					.serverSideEncryption(ServerSideEncryption.AWS_KMS).ssekmsKeyId("kmsKeyARN").
					uploadId("uploadId").build(); 
			Mockito.when(s3Client.createMultipartUpload(Mockito.any(CreateMultipartUploadRequest.class)))
					.thenReturn(createMultipartUploadResponse);
			CreateMultipartUploadResponse response = s3ClientDemo.s3CreateMultipartUpload("src/test/resources/s3InputFile.txt");
			Mockito.verify(s3Client).createMultipartUpload(Mockito.eq(createMultipartUploadRequest));
	        assertEquals(createMultipartUploadResponse.serverSideEncryption(), response.serverSideEncryption());
	        assertEquals(createMultipartUploadResponse.uploadId(), response.uploadId());
	        
			UploadPartRequest uploadPartRequest = UploadPartRequest.builder().bucket("bucket").key("key")
			.uploadId(response.uploadId())
			.partNumber(10)
			.build();
			UploadPartResponse uploadPartMockResponse = UploadPartResponse.builder()
					.serverSideEncryption(ServerSideEncryption.AWS_KMS).ssekmsKeyId("kmsKeyARN")
					.build();
			Mockito.when(s3Client.uploadPart(Mockito.any(UploadPartRequest.class),Mockito.any(RequestBody.class))).thenReturn(uploadPartMockResponse); 
			UploadPartResponse uploadPartResponse = s3ClientDemo.s3UploadPartTest("src/test/resources/s3InputFile.txt");
			
			Mockito.verify(s3Client).uploadPart(Mockito.eq(uploadPartRequest),Mockito.any(RequestBody.class));
			assertEquals(uploadPartResponse.ssekmsKeyId(), uploadPartMockResponse.ssekmsKeyId());
	    	System.out.println("end uploadPartTest class **********" + s3Client);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	@Test
	public void completeMultipartUploadTest() {
		try {
			System.out.println("start completeMultipartUploadTest class **********");
			List<CompletedPart> parts = new ArrayList<>();
			CompleteMultipartUploadResponse completeMultipartUploadResponse = CompleteMultipartUploadResponse
					.builder().bucket("bucket")
					.key("key").build();
			
			CreateMultipartUploadResponse createMultipartUploadResponse = CreateMultipartUploadResponse.builder()
					.bucket("bucket").key("key")
					.serverSideEncryption(ServerSideEncryption.AWS_KMS).ssekmsKeyId("kmsKeyARN").uploadId("uploadId").build();
			
			CompleteMultipartUploadRequest completeMultipartUploadRequest = CompleteMultipartUploadRequest.builder().bucket("bucket")
					.key("key").uploadId(createMultipartUploadResponse.uploadId())
					.multipartUpload(CompletedMultipartUpload.builder().parts(parts).build()).build();

			Mockito.when(s3Client.createMultipartUpload(Mockito.any(CreateMultipartUploadRequest.class)))
					.thenReturn(createMultipartUploadResponse);
					
			Mockito.when(s3Client.completeMultipartUpload(Mockito.any(CompleteMultipartUploadRequest.class)))
			.thenReturn(completeMultipartUploadResponse);
			s3ClientDemo.completeMultipartUpload("src/test/resources/s3InputFile.txt");
			Mockito.verify(s3Client).completeMultipartUpload(Mockito.eq(completeMultipartUploadRequest));
	    	System.out.println("end completeMultipartUploadTest class **********");
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}

    @Test
	public void getMetaDataTest() {
		try {
			System.out.println("start getMetaDataTest class **********" + s3Client);
			Collection<S3Object> lisOfS3Objects = new ArrayList<S3Object>();
			lisOfS3Objects.add(S3Object.builder().key("test1.txt").lastModified(Instant.now()).build());
			lisOfS3Objects.add(S3Object.builder().key("test2.txt").lastModified(Instant.now()).build());

			Collection<CommonPrefix> lisOfCommonPrefix = new ArrayList<CommonPrefix>();
			lisOfCommonPrefix.add(CommonPrefix.builder().prefix("prefix1").build());
			lisOfCommonPrefix.add(CommonPrefix.builder().prefix("prefix2").build());
			ListObjectsV2Response builder = ListObjectsV2Response.builder().startAfter("startAfter").delimiter("/")
					.contents(lisOfS3Objects).nextContinuationToken("nextContinuationToken")
					.commonPrefixes(lisOfCommonPrefix).isTruncated(true).build();
			Mockito.when(s3Client.listObjectsV2(Mockito.any(ListObjectsV2Request.class))).thenReturn(builder);
			ListObjectsV2Response response = s3ClientDemo.getMetaData("src/test/resources/s3InputFile.txt");
			System.out.println(response.commonPrefixes().get(0).prefix());
			System.out.println(response.delimiter());
			System.out.println(response.contents().get(0).key());
			System.out.println(response.isTruncated());
			System.out.println(response.nextContinuationToken());
			List<S3Object> items = response.contents();
			S3Object item = items.get(0);
			LocalDateTime date = LocalDateTime.ofInstant(item.lastModified(), ZoneId.systemDefault());
			System.out.println("end getMetaDataTest ----------" + date);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
}
