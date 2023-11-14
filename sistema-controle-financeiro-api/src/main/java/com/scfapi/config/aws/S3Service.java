package com.scfapi.config.aws;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;
import com.scfapi.config.property.ScfApiProperty;

@Component
public class S3Service {
	
	private static final String PROTOCOLOURL = "\\\\";
	
	private static final String URLAWS = ".s3.amazonaws.com/";

	private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private ScfApiProperty property;
	
	public String salvarAnexo(MultipartFile arquivo) {
		
		AccessControlList acl = new AccessControlList();
		acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(arquivo.getContentType());
		objectMetaData.setContentLength(arquivo.getSize());
		
		String idArquivo = gerarIdUnico(arquivo.getOriginalFilename());
		
		try {
			PutObjectRequest objectRequest = new PutObjectRequest(
					property.getS3Key().getBucket(), 
					idArquivo, 
					arquivo.getInputStream(), objectMetaData)
					.withAccessControlList(acl);
			
			objectRequest.setTagging(new ObjectTagging(
					Arrays.asList(new Tag("expirar","true"))));
			
			amazonS3.putObject(objectRequest);
			
			if(logger.isDebugEnabled()) {
				logger.debug("Arquivo {} enviado com sucesso! " + arquivo.getOriginalFilename());
			}
			
			return idArquivo;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu algum problema ao enviar o arquivo para nuvem ", e);
		}
	}
	
	public String configuraUrl(String objeto) {
		return PROTOCOLOURL + property.getS3Key().getBucket() +
				URLAWS + objeto;
	}

	public void salvarNaBase(String objeto) {
		SetObjectTaggingRequest objectTaggingRequest = new SetObjectTaggingRequest(
				property.getS3Key().getBucket(), 
				objeto, 
				new ObjectTagging(Collections.emptyList()));
		
		amazonS3.setObjectTagging(objectTaggingRequest);
	}

	public void substituir(String objetoAntigo, String objetoNovo) {
		if (StringUtils.hasText(objetoAntigo)) {
			remover(objetoAntigo);
		}
		salvarNaBase(objetoNovo);
	}

	public void remover(String objeto) {
		DeleteObjectRequest objectRequest = new DeleteObjectRequest(
				property.getS3Key().getBucket(), objeto);
		amazonS3.deleteObject(objectRequest);
	}
	
	private String gerarIdUnico(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}
	
}
