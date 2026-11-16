import importlib.util
import json
from pathlib import Path
import tempfile
import unittest
import xml.etree.ElementTree as ET

spec = importlib.util.spec_from_file_location("verify", Path(__file__).with_name("verify.py"))
verify = importlib.util.module_from_spec(spec)
spec.loader.exec_module(verify)

class EvidenceTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name)
        self.meta = dict(run_id="10", attempt="2", candidate="head", revision="merge", tree="tree")
        for shard in ("1", "2"):
            folder = self.root / ("shard-" + shard)
            folder.mkdir()
            names = [p.stem for p in verify.selected(shard)]
            (folder / "provenance.json").write_text(json.dumps(dict(self.meta, shard=shard, cases=names)))
            root = ET.Element("testsuites")
            suite = ET.SubElement(root, "testsuite", tests="6", failures="0")
            for name in names:
                ET.SubElement(suite, "testcase", name=name, file="/workspace/" + name + ".yaml", status="SUCCESS", time="1")
            ET.ElementTree(root).write(folder / "junit.xml")
    def mutate(self, operation):
        path = self.root / "shard-1/junit.xml"
        xml = ET.parse(path)
        operation(xml.getroot().find("testsuite"))
        xml.write(path)
    def reject(self):
        with self.assertRaises((ValueError, FileNotFoundError)):
            verify.inspect(self.root, self.meta)
    def test_complete(self):
        self.assertEqual(12, len(verify.inspect(self.root, self.meta)))
    def test_missing_case(self):
        self.mutate(lambda s: s.remove(s[0]))
        self.reject()
    def test_duplicate_case(self):
        self.mutate(lambda s: s.append(ET.fromstring(ET.tostring(s[0]))))
        self.reject()
    def test_failed_case(self):
        self.mutate(lambda s: ET.SubElement(s[0], "failure"))
        self.reject()
    def test_skipped_case(self):
        self.mutate(lambda s: s[0].set("status", "CANCELED"))
        self.reject()
    def test_skipped_element(self):
        self.mutate(lambda s: ET.SubElement(s[0], "skipped"))
        self.reject()
    def test_missing_shard(self):
        (self.root / "shard-2/junit.xml").unlink()
        self.reject()
    def test_wrong_attempt(self):
        meta = self.root / "shard-1/provenance.json"
        data = json.loads(meta.read_text())
        data["attempt"] = "1"
        meta.write_text(json.dumps(data))
        self.reject()
    def test_wrong_head(self):
        self.meta["candidate"] = "another"
        self.reject()
    def test_failure_counter(self):
        self.mutate(lambda s: s.set("failures", "1"))
        self.reject()

if __name__ == "__main__":
    unittest.main()
