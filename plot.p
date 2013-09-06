set term TERM size 800,600
set out IMG

set lmargin 15
set rmargin 20
set key out right

set multiplot layout 2, 1 title TITLE

unset xtics
set ylabel "cycles (CD)\n time/stepsize (ED)"
set bmargin 0.5

plot DATA_PS_CD using XCOL:1:xtic(XCOL) with linespoints title "CD-Push-Sum", \
     DATA_PS_ED using XCOL:($1/STEPSIZE) with linespoints title "ED-Push-Sum", \
     DATA_PP_CD using XCOL:1 with linespoints title "CD-Push-Pull", \
     DATA_PP_ED using XCOL:($1/STEPSIZE) with linespoints title "ED-Push-Pull"

set xtics right rotate by 45
set xlabel XLABEL
set ylabel "mean squared error"
set logscale y
set format y "%.0e"
set tmargin 0.5
set bmargin 3
unset key

plot DATA_PS_CD using XCOL:13:xtic(XCOL) with linespoints title "CD-Push-Sum", \
     DATA_PS_ED using XCOL:13 with linespoints title "ED-Push-Sum", \
     DATA_PP_CD using XCOL:13 with linespoints title "CD-Push-Pull", \
     DATA_PP_ED using XCOL:13 with linespoints title "ED-Push-Pull"
     
unset multiplot
set term pop