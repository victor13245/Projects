----------------------------------------------------------------------------
-- Nume proiect: clock
-- Nume modul:   displ7seg_blink
-- Descriere:    Multiplexor pentru afisajul cu sapte segmente, cu
--               posibilitatea palpairii cifrelor. Datele de intrare nu
--               sunt decodificate, ci sunt aplicate direct la segmentele
--               afisajului. Pentru afisarea valorilor hexazecimale,
--               codul fiecarei cifre trebuie aplicat la intrarea Data
--               prin intermediul functiei de conversie HEX2SSEG. Pentru
--               afisarea unei cifre cu palpaire, bitul 7 al codului cifrei
--               trebuie setat la 1.
--               Codificarea segmentelor (biti 7..0): 0GFE DCBA
--                   A
--                  ---  
--               F |   | B
--                  ---    <- G
--               E |   | C
--                  --- 
--                   D
----------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;

entity displ7seg_basys is
   Port ( Clk  : in  STD_LOGIC;
          Rst  : in  STD_LOGIC;
          Data : in  STD_LOGIC_VECTOR (15 downto 0); 
                 -- date de afisat (cifra 1 din stanga: biti 63..56)
          An   : out STD_LOGIC_VECTOR (3 downto 0); 
                 -- semnale pentru anozi (active in 0 logic)
          Cat  : out STD_LOGIC_VECTOR (7 downto 0)); 
                 -- semnale pentru segmentele (catozii) cifrei active
end displ7seg_basys;

architecture Behavioral of displ7seg_basys is

constant CLK_RATE  : INTEGER := 100_000_000;  -- frecventa semnalului Clk
constant CNT_100HZ : INTEGER := 2**20;        -- divizor pentru rata de
                                              -- reimprospatare de ~100 Hz
constant CNT_500MS : INTEGER := CLK_RATE / 2; -- divizor pentru 500 ms
signal Count       : INTEGER range 0 to CNT_100HZ - 1 := 0;
signal CountVect   : STD_LOGIC_VECTOR (19 downto 0) := (others => '0');
signal LedSel      : STD_LOGIC_VECTOR (1 downto 0) := (others => '0');
signal Digit1      : STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
signal Digit2      : STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
signal Digit3      : STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
signal Digit4      : STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
signal Seg : std_logic_vector(3 DOWNTO 0):="0000";
begin
   -- Proces pentru divizarea frecventei ceasului
   div_clk: process (Clk)
   begin
      if RISING_EDGE (Clk) then
         if (Rst = '1') then
            Count <= 0;
         elsif (Count = CNT_100HZ - 1) then
            Count <= 0;
         else
            Count <= Count + 1;
         end if;
      end if;
   end process div_clk;

   CountVect <= CONV_STD_LOGIC_VECTOR (Count, 20);
   LedSel <= CountVect (19 downto 18);

   -- Date pentru segmentele fiecarei cifre
   Digit1 <= Data(3 downto 0);
   Digit2 <= Data(7 downto 4);
   Digit3 <= Data(11 downto 8);
   Digit4 <= Data(15 downto 12);

   -- Semnal pentru selectarea cifrei active (anozi)
   An <= "1110" when LedSel = "00" else
         "1101" when LedSel = "01" else
         "1011" when LedSel = "10" else
         "0111" when LedSel = "11" else
         "1111";

   -- Semnal pentru segmentele cifrei active (catozi)
   Seg <= Digit1 when LedSel = "00" else
          Digit2 when LedSel = "01" else
          Digit3 when LedSel = "10" else
          Digit4 when LedSel = "11" else
          x"F";
          
    process
    begin 
    case Seg is
      when "0000" => Cat <= "11000000";  -- 0
      when "0001" => Cat <= "11111001";  -- 1
      when "0010" => Cat <= "10100100";  -- 2
      when "0011" => Cat <=  "10110000";  -- 3
      when "0100" => Cat <=  "10011001";  -- 4
      when "0101" => Cat <=  "10010010";  -- 5
      when "0110" => Cat <=  "10000010";  -- 6
      when "0111" => Cat <=  "11111000";  -- 7
      when "1000" => Cat <=  "10000000";  -- 8
      when "1001" => Cat <=  "10010000";  -- 9
      when "1010" => Cat <=  "10001000";  -- A
      when "1011" => Cat <=  "10000011";  -- b
      when "1100" => Cat <=  "11000110";  -- C
      when "1101" => Cat <=  "10100001";  -- d
      when "1110" => Cat <=  "10000110";  -- E
      when "1111" => Cat <= "10001110";  -- F
      when others => Cat <=  "11111111";
   end case;
   end process;
end Behavioral;