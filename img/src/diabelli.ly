\version "2.7.40"
% $Revision: 1.1 $

\header {
    title = "28 melodische Übungsstücke"
    subtitle = "4."
    composer = "Anton Diabelli"
    opus = "Op 149"

    mutopiatitle = "28 melodische Übungsstücke"
    mutopiacomposer = "Anton Diabelli"
    mutopiaopus = "Opus 149-4"
    mutopiainstrument = "Piano, Piano"
    source = "If I could know..."
    style = "Classical"
    copyright = "MutopiaBSD"
    maintainer = "Alberto Simões"
    maintainerEmail = "ambs@cpan.org"
    maintainerWeb = "http://alfarrabio.di.uminho.pt/~albie"
    lastupdated = "2004/Jan/17"

    tagline = "This music is part of the Mutopia project: http://www.MutopiaProject.org/"
    footer = "Mutopia-2004/01/17-404"
}

primoDynamics =  {
    s1\p s1 s1 s1 s1\f
    s1 s1 s1 s1\f s1 s1\p s1
    s1\f s1 s1 s8\> s2 s8\! s4 s1\p s1
    s1 s1 s1\f s1 s1 s1
}

primoUp =  {
    \time 4/4
    \clef treble
    \relative c''' {
	#(set-octavation 1)
        \override Score.OttavaBracket   #'padding = #2
	#(set-accidental-style 'modern)
	
	c8-1[( d e d] c-1[ d e f-4])
	g4-.-5 e-.-3 c-. e-.
	f8-4[( e d f]) e-3[( d c e])
	d4-.-2 g-5-. d-. g-.
	c,8-1[( d e d] c-1[ d e f-4)]

	g4-.-5 e-.-3 c-. e-.
	d-.-2 g-.-5 f8-4([ e f-4 d-2)]
	c4-1-. e-3-. c-. r

	\repeat volta 2 {
	    d8-2( e fis g-5) r2
	    d8-2( e fis g) r2
	    fis4-4-. d-2-. e-. fis-.
	    g2( d2)

	    d8-2( e fis g) r2
	    d8-2( e fis g) r2
	    fis4-4-. d-2-. e-. fis-.
	    g2.-5( f8-4 d-2)
	    
	    c8-1[( d e d] c-1[ d e f-4])
	    g4-.-5 e-.-3 c-. e-.
	    f8-4[( e d f]) e-3[( d c e])
	    d4-.-2 g-5-. d-. g-.
	    c,8-1[( d e d] c-1[ d e f-4)]
	    
	    g4-.-5 e-.-3 c-. e-.
	    d-.-2 g-.-5 f8-4([ e f-4 d-2)]
	    c4-1-. e-3-. c-. r
	}
    }
}

primoDown =  {
    \time 4/4
    \clef treble
    \relative c'' {
	<c-5 e-3>1
	<c e>2 <c e>
	<d-4 f-2> <c e>
	d4-.-4 g-.-1 d-. g-.

	<c,-5 e-3>1
	<c e>2 <c e>
	<d-4 f-2> <d f>
	<c-5 e-3>4-. <c-5 e-3>-. <c-5 e-3>-. r
	\repeat volta 2 {
	    r2 d8-4( e fis g)
	    r2 d8-4( e fis g)
	    fis4-2-. d-4-. e-. fis-.
	    g2-1( d-4)

	    r2 d8-4( e fis g)
	    r2 d8-4( e fis g)
	    fis4-2-. d-4-. e-. fis-.
	    g2.-1( f8-2 d-4)
	    
	    <c-5 e-3>1
	    <c e>2 <c e>
	    <d-4 f-2> <c e>
	    d4-.-4 g-.-1 d-. g-.

	    <c,-5 e-3>1
	    <c e>2 <c e>
	    <d-4 f-2> <d f>
	    <c-5 e-3>4-. <c e>-. <c e>-. r
	}
    }
}

secondoDynamics =  {
    s1\p s1 s1 s1 s1\f s1 s1 s1
    s1\f s1 s1\p s1 s1\f s1 s1 s1
    s1\p s1 s1 s1 s1\f s1 s1 s1
}

secondoUp =  {
    \time 4/4
    \clef bass
    \relative c' {
	<c-2 e-4>4( g-1 <c e> g)
	<c e>( g <c e> g)
	<d'-3 f-5>( g, <c-2 e-4> g)
	<b-2 d-3>( g <b d> g)
	<c-2 e-4>4( g <c e> g)
	<c e>( g <c e> g)
	<b-2 g'-5>( g <b g'> g)
	<g-1 c-3 e-5> <g c e> <g c e> r

	\repeat volta 2 {
	    <g-1 b-3 d-5>-.( <g b d>-. <g b d>-. <g b d>-.)
	    <g b d>-.( <g b d>-. <g b d>-. <g b d>-.)
	    <a-2 c-4 d-5>-.( <a c d>-. <a c d>-. <a c d>-.)
	    <g-1 b-3 d-5>-.( <g b d>-. <g b d>-. <g b d>-.)
	    <g b d>-.( <g b d>-. <g b d>-. <g b d>-.)
	    <g b d>-.( <g b d>-. <g b d>-. <g b d>-.)
	    <a-2 c-4 d-5>-. \clef treble <d-1 fis-3> <cis-2 g'-4> <c-1 a'-5>
	    << {b4-1 c-2 d-3 b-1} \\ {g'1-5}  >>

	    \clef bass
	    <c,-2 e-4>4( g-1 <c e> g)
	    <c e>( g <c e> g)
	    <d'-3 f-5>( g, <c-2 e-4> g)
	    <b-2 d-3>( g <b d> g)
	    <c-2 e-4>4( g <c e> g)
	    <c e>( g <c e> g)
	    <b-2 g'-5>( g <b g'> g)
	    <g-1 c-3 e-5> <g c e>-. <g c e>-. r
	}
    }
}

secondoDown =  {
    \time 4/4
    \clef bass
    \relative c {
	c4-1 r r2
	c,4 r c' r
	b-2 r c r
	g-4 r g-3 r
	c-1 r r2
	c,4 r c' r
	g-1 r g, r
	c-3 <c-5 c'> <c c'> r

	\repeat volta 2 {
	    g'-3 r b-2 r
	    g r b r
	    b, r d'-1 r
	    g,-3 r r2
	    g4-3 r b-2 r
	    g r b r
	    d r d, r
	    g-4( a-3 b-2 g-4)
	    c-1 r r2
	    c,4 r c' r
	    b-2 r c r
	    g-4 r g-3 r
	    c-1 r r2
	    c,4 r c' r
	    g-1 r g, r
	    c-3 <c-5 c'> <c c'> r
	}
    }
}

\score{
    \context PianoStaff  <<
	\set PianoStaff.instrument = "Secondo     " 
	\context Staff = "up"   \secondoUp
	\context Dynamics = "dynamics" \secondoDynamics
	\context Staff = "down" \secondoDown
    >>
    \layout {
	\context {
	    \type "Engraver_group"
	    \name Dynamics
	    \consists "Output_property_engraver"
      
	    \override VerticalAxisGroup #'minimum-Y-extent = #'(-1 . 1)
      
	    \consists "Script_engraver"
	    \consists "Dynamic_engraver"
	    \consists "Text_engraver"
	    
	    \override TextScript #'font-size = #2
	    \override TextScript #'font-shape = #'italic
	    \override DynamicText #'extra-offset = #'(0 . 2.0)
	    \override Hairpin #'extra-offset = #'(0 . 2.0)
	    
	    \consists "Skip_event_swallow_translator"
	    
	    \consists "Axis_group_engraver"
	}
	\context {
	    \PianoStaff
	    \accepts Dynamics
	    \override VerticalAlignment #'forced-distance = #7
	}
    }
    \header { piece = "Allegro." }
}
  

\score{
    
    \context PianoStaff <<
	\set PianoStaff.instrument = "Primo     " 
	\context Staff = "up"   \primoUp
	\context Dynamics = "dynamics" \primoDynamics
	\context Staff = "down" \primoDown
    >>
    \layout {
	\context {
	    \type "Engraver_group"
	    \name Dynamics
	    \consists "Output_property_engraver"
      
	    \override VerticalAxisGroup #'minimum-Y-extent = #'(-1 . 1)
      
	    \consists "Script_engraver"
	    \consists "Dynamic_engraver"
	    \consists "Text_engraver"
	    
	    \override TextScript #'font-size = #2
	    \override TextScript #'font-shape = #'italic
	    \override DynamicText #'extra-offset = #'(0 . 2.0)
	    \override Hairpin #'extra-offset = #'(0 . 2.0)
	    
	    \consists "Skip_event_swallow_translator"
	    
	    \consists "Axis_group_engraver"
	}
	\context {
	    \PianoStaff
	    \accepts Dynamics
	    \override VerticalAlignment #'forced-distance = #7
	}
    }
    \header { piece = "Allegro." }
}

\score{
    \context PianoStaff  <<
	\context Staff = "up"   <<
	    \applyMusic #unfold-repeats \primoUp
	    \applyMusic #unfold-repeats \secondoUp
	>>
	\context Staff = "down" <<
	    \applyMusic #unfold-repeats \primoDown
	    \applyMusic #unfold-repeats \secondoDown
	>>
    >>
    \midi { \tempo 4 = 120 }
}

