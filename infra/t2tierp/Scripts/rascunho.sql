select * from empresa e ;


ALTER TABLE public.empresa RENAME COLUMN contato TO empresa_contato;

ALTER TABLE public.empresa ADD tipo_controle_estoque varchar(50) NULL;

select * from contador c ;