package fr.an.tools.git2neo4j.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label="DirTree")
public class DirTreeEntity extends RevTreeEntity {

	// @RelatedTo()
	@Relationship(type="has_entry")
	private List<DirEntryEntity> entries = new ArrayList<>();
	
	// ------------------------------------------------------------------------

	public DirTreeEntity() {
	}

	// ------------------------------------------------------------------------
	
	public List<DirEntryEntity> getEntries() {
		return entries;
	}

	public void setEntries(List<DirEntryEntity> entries) {
		this.entries = entries;
	}

	public void addEntry(DirEntryEntity e) {
		if (entries == null) {
			entries = new ArrayList<>();
		}
		entries.add(e);
	}
	
	
	// ------------------------------------------------------------------------

	
}
