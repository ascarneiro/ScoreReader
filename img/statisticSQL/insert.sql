/*
 * input data for testset
 */

-- notation types
insert into notationtype (nr,name) values ('M','modern');
insert into notationtype (nr,name) values ('H','historic (chant, mensural)');
insert into notationtype (nr,name) values ('T','tablature');

-- music typesetting software
insert into notationprogram (nr,name,author,url) 
    values ('abc','abctab2ps','Christoph Dalitz',
            'http://www.lautengesellschaft.de/cdmm/');
insert into notationprogram (nr,name,author,url)
    values ('tab','lute tab','Wayne Cripps',
            'http://www.cs.dartmouth.edu/~wbc/lute/AboutTab.html');
insert into notationprogram (nr,name,author,url)
    values ('ly','LilyPond','Han-Wen Nienhuys, Jan Nieuwenhuizen',
            'http://www.lilypond.org/');
insert into notationprogram (nr,name,author,url)
    values ('dja','Django','Alain Veylit',
            'http://webpages.charter.net/django/');
insert into notationprogram (nr,name,author,url)
    values ('pmw','Philip''s Music Writer','Philip Hazel',
            'http://www.quercite.com/pmw.html');
insert into notationprogram (nr,name,author,url)
    values ('mup','Music Publisher','Arkkra enterprises',
            'http://www.arkkra.com/');
insert into notationprogram (nr,name,author,url)
    values ('fin','Finale','MakeMusic, Inc.',
            'http://www.finalemusic.com/');
insert into notationprogram (nr,name,author,url)
    values ('pmx','MusicTex preprocessor','Don Simons',
            'http://icking-music-archive.org/software/pmx/');
insert into notationprogram (nr,name,author,url)
    values ('tex','MusixTex','Daniel Taupin',
            'http://www.ctan.org/tex-archive/macros/musixtex/taupin/');
insert into notationprogram (nr,name,author,url)
    values ('nwc','NoteWorthy Composer','NoteWorthy Software, Inc.',
            'http://www.noteworthysoftware.com/');

-- test music files
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('001','tablature/carcassi.ps','T',7,'abc');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('002','tablature/dacrema.ps','T',8,'abc');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('003','tablature/demoy.ps','T',7,'dja');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('004','tablature/dowland.ps','T',5,'tab');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('005','tablature/gaultier01.ps','T',8,'dja');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('007','tablature/goess.ps','T',5,'tab');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('008','tablature/hotman.ps','T',4,'tab');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('009','tablature/hurel.ps','T',6,'dja');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('010','tablature/baroque.ps','T',8,'dja');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('011','historic/derore01.ps','H',7,'ly');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('013','historic/gregorian.ps','H',3,'ly');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('017','historic/josquin04.ps','H',12,'abc');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('018','historic/rossi.ps','H',12,'abc');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('019','historic/ockeghem.ps','H',10,'tex');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('020','modern/williams.ps','M',12,'abc');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('021','modern/wagner.ps','M',9,'pmw');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('022','modern/victoria09.ps','M',15,'abc');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('024','modern/tye.ps','M',12,'mup');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('025','modern/schumann.ps','M',8,'ly');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('026','modern/rameau.ps','M',8,'ly');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('029','modern/pmw04.ps','M',10,'pmw');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('030','modern/pmw03.ps','M',12,'pmw');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('031','modern/pmw01.ps','M',16,'pmw');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('032','modern/mahler.ps','M',9,'pmw');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('033','modern/diabelli.ps','M',8,'ly');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('035','modern/dalitz03.ps','M',12,'mup');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('037','modern/carcassi01.ps','M',8,'ly');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('038','modern/buxtehude.ps','M',15,'tex');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('040','modern/bruckner01.ps','M',12,'fin');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('041','modern/brahms02.ps','M',12,'fin');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('043','modern/bellinzani.ps','M',12,'abc');
insert into musicsample (nr,filename,typenr,staffs,programnr)
    values ('044','modern/bach.ps','M',7,'ly');
