ALTER TABLE translations
      DROP COLUMN orig_id,
      DROP COLUMN translation_id,
      ADD COLUMN from_lang text,
      ADD COLUMN to_lang text,
      ADD CONSTRAINT from_lang_fk FOREIGN KEY (from_lang) REFERENCES languages (name),
      ADD CONSTRAINT to_lang_fk FOREIGN KEY (to_lang) REFERENCES languages (name);
