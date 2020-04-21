package com.dna.bifincan.model.products;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name="image")
public class Image extends BaseEntity implements Serializable {
	@Lob
	@Column(name = "contents", nullable = false)
	private byte[] contents;
        
        @Column(name="media_length_in_bytes", nullable = false)
        private int mediaLengthInBytes;
        
	public Image() { }
	
	public Image(byte[] contents) {
		super();
		this.contents = contents;
                this.mediaLengthInBytes = contents.length;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

        public int getMediaLengthInBytes() {
            return mediaLengthInBytes;
        }

        public void setMediaLengthInBytes(int mediaLengthInBytes) {
            this.mediaLengthInBytes = mediaLengthInBytes;
        }
        
        

}
