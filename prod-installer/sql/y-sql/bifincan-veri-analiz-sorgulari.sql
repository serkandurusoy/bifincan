select count(*) from babylon;

select soru,yanit,count(yanit) from babylon where tamamen_temiz = 'Evet' group by soru,yanit order by soru,yanit;

select count(distinct concat(soru,yanit)) from babylon;

select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid;

select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet';

select rs.cinsiyet,count(rs.cinsiyet)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet' ) as yuzde,count(rs.cinsiyet) as sayi from (select sruid,cinsiyet,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.cinsiyet;

select rs.egitim_durumu,count(rs.egitim_durumu)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,count(rs.egitim_durumu) as sayi from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.egitim_durumu;

select rs.sehir,count(rs.sehir)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,count(rs.sehir) as sayi from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.sehir;

select rs.medeni_durum,count(rs.medeni_durum)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,count(rs.medeni_durum) as sayi from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.medeni_durum;

select rs.cocuk,count(rs.cocuk)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,count(rs.cocuk) as sayi from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.cocuk;

select rs.calisma_durumu,count(rs.calisma_durumu)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,count(rs.calisma_durumu) as sayi from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.calisma_durumu;

select rs.aylik_geliri,count(rs.aylik_geliri)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,count(rs.aylik_geliri) as sayi from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.aylik_geliri;

select rs.gsm_operatoru,count(rs.gsm_operatoru)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,count(rs.gsm_operatoru) as sayi from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by rs.gsm_operatoru;

select 
    case 
        when rs.yas <= 18 then '14 - 18'
        when 18 < rs.yas and rs.yas <= 22 then '19 - 22'
        when 22 < rs.yas and rs.yas <= 26 then '23 - 26'
        when 26 < rs.yas and rs.yas <= 30 then '27 - 30'
        when 30 < rs.yas and rs.yas <= 35 then '31 - 35'
        when 35 < rs.yas and rs.yas <= 40 then '36 - 40'
        when 40 < rs.yas and rs.yas <= 50 then '41 - 50'
        when 50 < rs.yas then '50 +'
    end as yas_araligi, 
	count(*)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet') as yuzde,
	count(*) as sayi
from (select sruid,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' group by sruid) as rs
group by yas_araligi;


select
	rs.soru,
	rs.yanit,
	count(rs.yanit) as sayi
from
	(select soru,yanit from babylon where tamamen_temiz = 'Evet') as rs
group by
	rs.soru,rs.yanit;

select count(distinct sruid) from babylon where tamamen_temiz = 'Evet' and soru = 'Festivale giderken hangi ulaşım yollarını tercih edersin?';



select
	rs.soru,
	rs.yanit,
	count(rs.yanit) as sayi
from
	(select soru,yanit from babylon where tamamen_temiz = 'Evet' and soru = 'babylon\'yı ilk nerede gördün veya duydun?') as rs
group by
	rs.soru,rs.yanit;

select * from babylon where tamamen_temiz = 'Evet' and
sruid in (select r1.sruid from babylon r1 where r1.soru = 'Kasımpaşa Spor seni bir maçına davet etse, gider miydin?' and r1.yanit <> 'Hayır');


select
	rs.soru,
	rs.yanit,
	count(rs.yanit) as sayi
from
	(select soru,yanit from babylon where tamamen_temiz = 'Evet' and soru = 'Aşağıdakilerden hangileri olsa stadyumda maç izlemeye gidersin?' and cinsiyet = 'Erkek') as rs
group by
	rs.soru,rs.yanit;

select * from babylon where tamamen_temiz = 'Evet' and
sruid in (select r1.sruid from babylon r1 where r1.soru = 'Kasımpaşa Spor seni bir maçına davet etse, gider miydin?' and r1.yanit <> 'Hayır') and
sruid in (select r2.sruid from babylon r2 where r2.soru = 'babylon\'nın televizyon programını izliyor musun?' and r2.yanit in ('Denk gelirse','Evet'));

select 
    rs.aylik_geliri, 
	count(*)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet'
		 and ct.yanit = '4 - 6 TL arası'
		) as yuzde,
	count(*) as sayi
from
	(select aylik_geliri from babylon where tamamen_temiz = 'Evet' and yanit = '4 - 6 TL arası'
	group by sruid)
	as rs
group by aylik_geliri;


select 
    case 
        when rs.yas <= 18 then '14 - 18'
        when 18 < rs.yas and rs.yas <= 22 then '19 - 22'
        when 22 < rs.yas and rs.yas <= 26 then '23 - 26'
        when 26 < rs.yas and rs.yas <= 30 then '27 - 30'
        when 30 < rs.yas and rs.yas <= 35 then '31 - 35'
        when 35 < rs.yas and rs.yas <= 40 then '36 - 40'
        when 40 < rs.yas and rs.yas <= 50 then '41 - 50'
        when 50 < rs.yas then '50 +'
    end as yas_araligi, 
	count(*)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet'
		 and ct.soru = 'Peki babylon Ultimate Repair serisi desem?' and ct.yanit = 'Hah, duydum'
		) as yuzde,
	count(*) as sayi
from
	(select yas from babylon where tamamen_temiz = 'Evet' and soru = 'Peki babylon Ultimate Repair serisi desem?' and yanit = 'Hah, duydum'
	group by sruid)
	as rs
group by yas_araligi;



select rs.cinsiyet,count(rs.cinsiyet)/(select count(distinct ct.sruid) from babylon ct where ct.tamamen_temiz = 'Evet' and sruid in (select r1.sruid from babylon r1 where r1.soru = 'Aşağıdakilerden hangileri olsa stadyumda maç izlemeye gidersin?')) as yuzde,count(rs.cinsiyet) as sayi from (select sruid,cinsiyet,yas,sehir,medeni_durum,cocuk,egitim_durumu,calisma_durumu,aylik_geliri,gsm_operatoru from babylon where tamamen_temiz = 'Evet' and sruid in (select r1.sruid from babylon r1 where r1.soru = 'Aşağıdakilerden hangileri olsa stadyumda maç izlemeye gidersin?')group by sruid) as rs
group by rs.cinsiyet;

