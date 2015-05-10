ALTER TABLE translations
      DROP COLUMN from_lang,
      DROP COLUMN to_lang,
      ADD COLUMN orig_id,
      ADD COLUMN translation_id,
      ADD CONSTRAINT orig_id_fk FOREIGN KEY (orig_id) REFERENCES languages (id),
      ADD CONSTRAINT translation_id_fk FOREIGN KEY (translation_id) REFERENCES languages (id);
