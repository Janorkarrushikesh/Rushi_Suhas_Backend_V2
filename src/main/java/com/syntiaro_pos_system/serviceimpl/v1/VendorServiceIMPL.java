package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.Vendor;

import com.syntiaro_pos_system.repository.v1.VendorRepo;
import com.syntiaro_pos_system.service.v1.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VendorServiceIMPL implements VendorService {
	private final VendorRepo vendorRepo;

	@Autowired
	public VendorServiceIMPL(VendorRepo vendorRepo) {
		this.vendorRepo = vendorRepo;
	}

	public Vendor createVendor(Vendor vendor) {
		Integer store_id = vendor.getStoreId();
		String vendor_name = vendor.getVendorName();
		if (vendorRepo.existsByStoreIdAndVendorName(store_id, vendor_name)) {
			throw new RuntimeException("Vendor with the same name already exists in this store.");
		}
		return vendorRepo.save(vendor);
	}
	@Override
	public String addVendor(@RequestBody Vendor vendor) {
		Integer lastBillNumbers = vendorRepo.findMaxVendorIdByStoreId(vendor.getStoreId());
		System.out.println(vendor.getStoreId() +" store id");
		System.out.println(lastBillNumbers + " lastbill no");
		vendor.setVendorId(lastBillNumbers != null ? lastBillNumbers + 1 : 1);
		vendorRepo.save(vendor);
		return vendor.getVendorName();
	}
	@Override
	public List<Vendor> getVendors() {
		return vendorRepo.findAll();
	}
	@Override
	public Vendor updateVendor(Vendor vendor) {
		vendorRepo.save(vendor);
		return vendor;
	}
	public Vendor saveDetails(Vendor vendor) {
		return vendorRepo.save(vendor);

	}
	public Vendor getVendorDetailsById(int id) {
		return vendorRepo.findById((long) id).orElse(null);
	}
	@Override
	public List<Vendor> fetchVendorsBystoreId(Integer storeId) {
		return vendorRepo.findBystore_id(storeId);
	}


	@Override
	public Vendor getVendorDetailsById(Integer id) {
		return vendorRepo.findById((long) id).orElse(null);
	}


	// THIS METHOD IS USE FOR UPDATE VENDOR
	@Override
	public Vendor updateVendor(Integer vendor_id, Vendor vendor) {
		Optional<Vendor> existingVendor = vendorRepo.findById((long) Integer.parseInt(String.valueOf((vendor_id))));
		if (existingVendor.isPresent()) {
			Vendor updatevendor = existingVendor.get();

			if (vendor.getVendorName() != null) {
				updatevendor.setVendorName(vendor.getVendorName());
			}
			if (vendor.getVendorAddress() != null) {
				updatevendor.setVendorAddress(vendor.getVendorAddress());
			}
			if (vendor.getMobileNo() != null) {
				updatevendor.setMobileNo(vendor.getMobileNo());
			}
			if (vendor.getGstNo() != null) {
				updatevendor.setGstNo(vendor.getGstNo());
			}
			if (vendor.getUpdateDate() != null) {
				updatevendor.setUpdateDate(vendor.getUpdateDate());
			}
			if (vendor.getUpdatedBy() != null) {
				updatevendor.setUpdatedBy(vendor.getUpdatedBy());
			}
			if (vendor.getCreatedDate() != null) {
				updatevendor.setCreatedDate(vendor.getCreatedDate());
			}
			if (vendor.getCreatedBy() != null) {
				updatevendor.setCreatedBy(vendor.getCreatedBy());
			}
			if (vendor.getStoreId() != null) {
				updatevendor.setStoreId(vendor.getStoreId());
			}
			if (vendor.getBankName() != null) {
				updatevendor.setBankName(vendor.getBankName());
			}
			if (vendor.getBranch() != null) {
				updatevendor.setBranch(vendor.getBranch());
			}
			if (vendor.getAccountNo() != null) {
				updatevendor.setAccountNo(vendor.getAccountNo());
			}
			if (vendor.getIfscCode() != null) {
				updatevendor.setIfscCode(vendor.getIfscCode());
			}
			if (vendor.getUpiId() != null) {
				updatevendor.setUpiId(vendor.getUpiId());
			}
			if (vendor.getVendorCode() != null) {
				updatevendor.setVendorCode(vendor.getVendorCode());
			}

			vendorRepo.save(updatevendor);
			return updatevendor;
		} else {
			return null;
		}
	}
	public byte[] findByStoreId(Integer store_id) throws IOException {
		String[] headers = {"Vendor ID", "Vendor Name", "Vendor Address", "Mobile No", "GST No", "Bank Name", "Branch", "Account No", "IFSC Code", "UPI ID"};

		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);

		try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
			float margin = 50;
			float yStart = page.getMediaBox().getHeight() - margin;
			float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
			float yPosition = yStart;
			float rowHeight = 20f;
			List<Vendor> vendorList = vendorRepo.findBystore_id(store_id);
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 6);
			float xPosition = margin;
			for (String header : headers) {
				contentStream.beginText();
				contentStream.newLineAtOffset(xPosition, yPosition - 15);
				contentStream.showText(header);
				contentStream.endText();
				xPosition += tableWidth / headers.length;
			}
			contentStream.setFont(PDType1Font.HELVETICA, 6);
			yPosition -= rowHeight;
			for (Vendor vendor : vendorList) {
				xPosition = margin;
				for (int i = 0; i < headers.length; i++) {
					String header = headers[i];
					String cellValue = getCellValue(vendor, header);
					contentStream.beginText();
					contentStream.newLineAtOffset(xPosition, yPosition - 15);
					contentStream.showText(cellValue);
					contentStream.endText();
					xPosition += tableWidth / headers.length; // Adjust the column width evenly
				}
				yPosition -= rowHeight;
			}
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		document.save(outputStream);
		document.close();
		return outputStream.toByteArray();
	}
	private String getCellValue(Vendor vendor, String header) {
		if ("Vendor ID".equals(header)) {
			return String.valueOf(vendor.getVendorId());
		} else if ("Vendor Name".equals(header)) {
			return vendor.getVendorName();
		} else if ("Vendor Address".equals(header)) {
			return vendor.getVendorAddress();
		} else if ("GST No".equals(header)) {
			return vendor.getGstNo();
		} else if ("Mobile No".equals(header)) {
			return String.valueOf(vendor.getMobileNo());
		} else if ("Bank Name".equals(header)) {
			return vendor.getBankName();
		} else if ("Branch".equals(header)) {
			return vendor.getBranch();
		} else if ("Account No".equals(header)) {
			return String.valueOf(vendor.getAccountNo());
		} else if ("IFSC Code".equals(header)) {
			return vendor.getIfscCode();
		} else if ("UPI ID".equals(header)) {
			return vendor.getUpiId();
		}
		return "";
	}



	/*-------THIS METHOD IS USE FOR UPDATE VENDOR--------*/
	/*---------CHANGES MADE BY TRUPTI-------*/
	@Override
	public ResponseEntity<?> updateVendorWithValidation(Long vendor_id, Vendor vendor) {
		Optional<Vendor> existingVendorOpt = vendorRepo.findById(vendor_id);
		if (existingVendorOpt.isPresent()) {
			Vendor existingVendor = existingVendorOpt.get();
			Integer storeId = existingVendor.getStoreId();

			// Check if the updated account number already exists for another vendor in the same store
			if (vendor.getAccountNo() != null && !vendor.getAccountNo().equals(existingVendor.getAccountNo())) {
				Vendor existingVendorWithSameAccount = vendorRepo.findByStoreIdAndAccountNo(storeId, vendor.getAccountNo());
				if (existingVendorWithSameAccount != null) {
					return ResponseEntity.badRequest().body("Account number already exists for another vendor in the same store");
				}
				existingVendor.setAccountNo(vendor.getAccountNo());
			}

			// Check if the updated UPI ID already exists for another vendor in the same store
			if (vendor.getUpiId() != null && !vendor.getUpiId().equals(existingVendor.getUpiId())) {
				Vendor existingVendorWithSameUpiId = vendorRepo.findByStoreIdAndUpiId(storeId, vendor.getUpiId());
				if (existingVendorWithSameUpiId != null) {
					return ResponseEntity.badRequest().body("UPI ID already exists for another vendor in the same store");
				}
				existingVendor.setUpiId(vendor.getUpiId());
			}
			if (vendor.getVendorName() != null) {
				existingVendor.setVendorName(vendor.getVendorName());
			}
			if (vendor.getMobileNo() != null) {
				existingVendor.setMobileNo(vendor.getMobileNo());
			}
			if (vendor.getGstNo()!= null) {
				existingVendor.setGstNo(vendor.getGstNo());
			}
			if (vendor.getVendorAddress() != null) {
				existingVendor.setVendorAddress(vendor.getVendorAddress());
			}
			if (vendor.getBranch() != null) {
				existingVendor.setBranch(vendor.getBranch());
			}
			if (vendor.getBankName() != null) {
				existingVendor.setBankName(vendor.getBankName());
			}
			if (vendor.getVendorCode() != null) {
				existingVendor.setVendorCode(vendor.getVendorCode());
			}
			// TRUPTI ADDED BECACUSE IFSC CODE NOT EDITE
			if (vendor.getIfscCode() != null) {
				existingVendor.setIfscCode(vendor.getIfscCode());
			}
			// Save the changes if any
			vendorRepo.save(existingVendor);
			return ResponseEntity.ok(existingVendor);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	/*----------TRUPTI CODE END HERE-------*/


}
