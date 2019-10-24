package com.souro.file_upload.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * The type Upload model.
 */
public class UploadModel {
	/**
	 * The Extra field.
	 */
	private String extraField;

	/**
	 * The File.
	 */
	private MultipartFile file;

	/**
	 * Gets extra field.
	 *
	 * @return the extra field
	 */
	public String getExtraField() {
		return extraField;
	}

	/**
	 * Sets extra field.
	 *
	 * @param extraField the extra field
	 */
	public void setExtraField(String extraField) {
		this.extraField = extraField;
	}

	/**
	 * Gets file.
	 *
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}

	/**
	 * Sets file.
	 *
	 * @param file the file
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "UploadModel{" + "extraField='" + extraField + '\'' + ", files=" + file + '}';
	}
}
