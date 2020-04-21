package com.dna.bifincan.service;

import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.repository.products.ImageRepository;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Named("imageService")
public class ImageService {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ImageService.class);

	@Inject
	ImageRepository imageRepository;

	public Image findById(Long Id) {
		return this.imageRepository.findOne(Id);
	}
}
